package com.example.cinemaapp

import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import android.content.Intent
import android.os.Handler
import android.text.Spannable
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log

class AuthActivity : ComponentActivity() {
    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var textViewError: TextView
    private lateinit var database: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_layout)

        editTextUsername = findViewById(R.id.editTextUsername)
        editTextPassword = findViewById(R.id.editTextPassword)
        textViewError = findViewById(R.id.textViewError)
        database = Database.getInstance(applicationContext)
        val firstStart = intent.getBooleanExtra("firstStart", false)
        if (firstStart) database.updateDatabase()

        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val textViewRegister = findViewById<TextView>(R.id.textViewToRegister)

        // Underline text
        val text = "Не зарегистрированы? Зарегистрироваться"
        val spannableString = SpannableString(text)
        spannableString.setSpan(
            UnderlineSpan(),
            text.indexOf("Зарегистрироваться"),
            text.indexOf("Зарегистрироваться") + "Зарегистрироваться".length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textViewRegister.text = spannableString
        // Underline text

        buttonLogin.setOnClickListener {
            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()

            Log.d("AuthActivity", "Username: $username, Password: $password")

            if (database.isUserValid(username, password)){
                val intent = Intent(this, AllFilmsActivity::class.java)
                intent.putExtra("USERNAME", username)
                startActivity(intent)
                finish()
            }
            else{
                showErrorMessage("Логин или пароль не верны")
            }
        }

        textViewRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showErrorMessage(message: String) {
        textViewError.text = message
        textViewError.visibility = TextView.VISIBLE

        val handler = Handler()
        handler.postDelayed({
            textViewError.visibility = TextView.INVISIBLE
        }, 3000)
    }
}
