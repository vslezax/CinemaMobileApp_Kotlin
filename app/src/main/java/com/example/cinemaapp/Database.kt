package com.example.cinemaapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.InputStream
import java.util.Scanner

class User(_username: String, _password: String) {
    val username: String = _username
    val password: String = _password
}

class Film(_title: String, _year: Int, _description: String, _picture: String) {
    val title: String = _title
    val year: Int = _year
    val description: String = _description
    val picture: String = _picture
}

class Database(_context: Context) :
    SQLiteOpenHelper(_context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        @Volatile
        private var instance: Database? = null

        fun getInstance(context: Context): Database {
            return instance ?: synchronized(this) {
                instance ?: Database(context.applicationContext).also { instance = it }
            }
        }

        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "FilmsAndUsersDB"
        private const val TABLE_FILMS = "films"
        private const val TABLE_USERS = "users"
        private const val TABLE_FAVORITES = "favorites"

        // Поля таблицы films
        private const val KEY_FILM_ID = "film_id"
        private const val KEY_TITLE = "title"
        private const val KEY_YEAR = "year"
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_PICTURE = "picture"

        // Поля таблицы users
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"

        // Поля таблицы favorites
        private const val KEY_USERNAME_FK = "username_fk_id"
        private const val KEY_FILM_FK = "film_fk_id"
    }
    private val context : Context = _context

    override fun onCreate(db: SQLiteDatabase) {
        val createFilmsTable = ("CREATE TABLE $TABLE_FILMS ($KEY_FILM_ID INTEGER PRIMARY KEY,"
                + "$KEY_TITLE TEXT,"
                + "$KEY_YEAR INTEGER,"
                + "$KEY_DESCRIPTION TEXT,"
                + "$KEY_PICTURE TEXT)")
        db.execSQL(createFilmsTable)
        readFilmsFile()

        // Создание таблицы users
        val createUsersTable = ("CREATE TABLE $TABLE_USERS ($KEY_USERNAME TEXT PRIMARY KEY,"
                + "$KEY_PASSWORD TEXT)")
        db.execSQL(createUsersTable)
        readUsersFile()

        // Создание таблицы favorites
        val createFavoritesTable = ("CREATE TABLE $TABLE_FAVORITES ($KEY_USERNAME_FK TEXT,"
                + "$KEY_FILM_FK INTEGER,"
                + "PRIMARY KEY ($KEY_USERNAME_FK, $KEY_FILM_FK),"
                + "FOREIGN KEY ($KEY_USERNAME_FK) REFERENCES $TABLE_USERS($KEY_USERNAME),"
                + "FOREIGN KEY ($KEY_FILM_FK) REFERENCES $TABLE_FILMS($KEY_FILM_ID))")
        db.execSQL(createFavoritesTable)
    }

    fun readFilmsFile(){
        val inputStream: InputStream = context.resources.openRawResource(R.raw.films)
        val scanner = Scanner(inputStream)
        while (scanner.hasNextLine()) {
            val filmData = scanner.nextLine()
            val filmAttributes = filmData.split(":")
            val film = Film(
                filmAttributes[0], // Название фильма
                filmAttributes[1].toInt(), // Год выпуска
                filmAttributes[3], // Описание
                filmAttributes[2] // Название изображения (ресурс)
            )
            addFilm(film) // Добавляем фильм в базу данных
        }
        scanner.close()
        inputStream.close()
    }

    fun readUsersFile(){
        val inputStream = context.resources.openRawResource(R.raw.users)
        val scanner = Scanner(inputStream)
        while (scanner.hasNextLine()) {
            val userData = scanner.nextLine()
            val userAttributes = userData.split(":")
            val user = User(
                userAttributes[0], // Никнейм
                userAttributes[1] // Пароль
            )
            addUser(user) // Добавляем юзера в базу данных
        }
        scanner.close()
        inputStream.close()
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Удаление старых таблиц при обновлении базы данных
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FAVORITES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FILMS")

        // Создание таблиц заново
        onCreate(db)
    }

    fun addFilm(film: Film) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_TITLE, film.title)
        values.put(KEY_YEAR, film.year)
        values.put(KEY_DESCRIPTION, film.description)
        values.put(KEY_PICTURE, film.picture)

        // Вставляем строку в таблицу films
        db.insert(TABLE_FILMS, null, values)
        db.close()
    }

    @SuppressLint("Range")
    fun addUser(user: User) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_USERNAME, user.username)
        values.put(KEY_PASSWORD, user.password)

        // Вставляем строку в таблицу users
        db.insert(TABLE_USERS, null, values)
        db.close()

        val db1 = this.readableDatabase
        val cursor = db1.rawQuery("SELECT * FROM $TABLE_USERS", null)

        if (cursor.moveToFirst()) {
            do {
                val username = cursor.getString(cursor.getColumnIndex(KEY_USERNAME))
                val password = cursor.getString(cursor.getColumnIndex(KEY_PASSWORD))
                Log.d("Database", "Username: $username, Password: $password")
            } while (cursor.moveToNext())
        }

        cursor.close()
    }

    fun addFavorite(username: String, filmId: Int) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_USERNAME_FK, username)
        values.put(KEY_FILM_FK, filmId)

        // Вставляем строку в таблицу favorites
        db.insert(TABLE_FAVORITES, null, values)
        db.close()
    }

    fun getAllFilms(): List<Film> {
        val filmList = mutableListOf<Film>()
        val selectQuery = "SELECT * FROM $TABLE_FILMS"

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val film = Film(
                    cursor.getString(0), // Индекс 0 соответствует столбцу KEY_TITLE
                    cursor.getInt(1), // Индекс 1 соответствует столбцу KEY_YEAR
                    cursor.getString(2), // Индекс 2 соответствует столбцу KEY_DESCRIPTION
                    cursor.getString(3) // Индекс 3 соответствует столбцу KEY_PICTURE
                )
                filmList.add(film)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return filmList
    }

    fun getFavoritesByUsername(username: String): List<Film> {
        val filmList = mutableListOf<Film>()
        val selectQuery = ("SELECT $TABLE_FILMS.* FROM $TABLE_FILMS "
                + "INNER JOIN $TABLE_FAVORITES ON $TABLE_FILMS.$KEY_FILM_ID = $TABLE_FAVORITES.$KEY_FILM_FK "
                + "WHERE $TABLE_FAVORITES.$KEY_USERNAME_FK = ?")

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, arrayOf(username))

        if (cursor.moveToFirst()) {
            do {
                val film = Film(
                    cursor.getString(0), // Индекс 0 соответствует столбцу KEY_TITLE
                    cursor.getInt(1), // Индекс 1 соответствует столбцу KEY_YEAR
                    cursor.getString(2), // Индекс 2 соответствует столбцу KEY_DESCRIPTION
                    cursor.getString(3) // Индекс 3 соответствует столбцу KEY_PICTURE
                )
                filmList.add(film)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return filmList
    }

    fun getUser(username: String): User? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USERS WHERE $KEY_USERNAME = ?", arrayOf(username))

        var user: User? = null

        if (cursor.moveToFirst()) {
            user = User(cursor.getString(0), cursor.getString(1))
        }

        cursor.close()
        return user
    }

    fun isUserValid(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val selection = "$KEY_USERNAME = ? AND $KEY_PASSWORD = ?"
        val selectionArgs = arrayOf(username, password)
        val cursor = db.query(
            TABLE_USERS,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val isValid = cursor.moveToFirst()
        cursor.close()
        return isValid
    }

    fun updateDatabase(){
        val db = this.writableDatabase
        db.delete(TABLE_FAVORITES, null, null)
        db.delete(TABLE_USERS, null, null)
        db.delete(TABLE_FILMS, null, null)

        readFilmsFile()
        readUsersFile()
    }
}