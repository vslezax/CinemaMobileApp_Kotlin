package com.example.cinemaapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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
        database = Database.getInstance(this)

        val registerButton: Button = findViewById(R.id.buttonRegister)
        registerButton.setOnClickListener {
            saveUserCredentials()
        }
    }

    private fun saveUserCredentials() {
        val login = registerEditText.text.toString()
        val password = passwordEditText.text.toString()

        val user = User(login, password)

        database.addUser(user)
    }
}
