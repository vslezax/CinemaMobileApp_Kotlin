package com.example.cinemaapp

import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import android.content.Intent
import android.os.Handler

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
        database = Database.getInstance(this)

        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val textViewRegister = findViewById<TextView>(R.id.textViewToRegister)

        buttonLogin.setOnClickListener {
            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()

            val isUserValid = isUserValid(username, password)

            if (isUserValid){
                val intent = Intent(this, MainActivity::class.java)
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

    private fun isUserValid(username: String, password: String): Boolean {
        val user = database.getUser(username)
        return user != null && user.password == password
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
