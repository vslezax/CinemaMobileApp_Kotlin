package com.example.cinemaapp

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity

class RegisterActivity : ComponentActivity() {
    private lateinit var registerEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var database: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_layout)

        registerEditText = findViewById(R.id.editTextUsername)
        passwordEditText = findViewById(R.id.editTextPassword)
        var loginTextView = findViewById<TextView>(R.id.textViewToLogin)
        database = Database.getInstance(this)

        // Underline text
        val text = "Уже зарегистрированы? Войти"
        val spannableString = SpannableString(text)
        spannableString.setSpan(
            UnderlineSpan(),
            text.indexOf("Войти"),
            text.indexOf("Войти") + "Войти".length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        loginTextView.text = spannableString
        // Underline text

        val registerButton: Button = findViewById(R.id.buttonRegister)
        registerButton.setOnClickListener {
            saveUserCredentials()
        }

        loginTextView.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }
    }

    private fun saveUserCredentials() {
        val login = registerEditText.text.toString()
        val password = passwordEditText.text.toString()

        val user = User(login, password)

        database.addUser(user)
    }
}
