package com.example.cinemaapp

import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import android.content.Intent

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_layout)

        val editTextUsername = findViewById<EditText>(R.id.editTextUsername)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val textViewRegister = findViewById<TextView>(R.id.textViewRegister)

        buttonLogin.setOnClickListener {
            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()

            val isUserValid = isUserValid(username, password)

            if (isUserValid){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        textViewRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun isUserValid(username: String, password: String): Boolean {
        val usersRaw = resources.openRawResource(R.raw.users)
        val usersReader = usersRaw.bufferedReader()

        usersReader.useLines { lines ->
            lines.forEach { line ->
                val (storedUsername, storedPassword) = line.split(":")
                if (username == storedUsername && password == storedPassword) {
                    return true
                }
            }
        }

        return false
    }
}