package com.example.cinemaapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AllFilmsActivity : AppCompatActivity() {
    private lateinit var username: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.all_films_layout)

        username = intent.getStringExtra("USERNAME") ?: ""

        val favoriteFilmsIcon = findViewById<ImageView>(R.id.favoriteFilms)
        val cinemasIcon = findViewById<ImageView>(R.id.cinemasLocation)

        var recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        var filmAdapter = FilmAdapter()
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = filmAdapter
        val films: List<Film> = Database.getInstance(this).getAllFilms()
        filmAdapter.setFilms(films)

        favoriteFilmsIcon.setOnClickListener {
            val intent = Intent(this, FavoriteFilmsActivity::class.java)
            startActivity(intent)
        }

        cinemasIcon.setOnClickListener {
            val intent = Intent(this, CinemasMapActivity::class.java)
            startActivity(intent)
        }
    }

    fun setUsername(_username: String){
        username = _username
    }
}
