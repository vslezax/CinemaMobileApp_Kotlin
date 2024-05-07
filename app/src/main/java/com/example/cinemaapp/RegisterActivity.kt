package com.example.cinemaapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import java.io.OutputStreamWriter

class RegisterActivity : ComponentActivity() {
    private lateinit var registerEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_layout)

        registerEditText = findViewById(R.id.editTextUsername)
        passwordEditText = findViewById(R.id.editTextPassword)

        val registerButton: Button = findViewById(R.id.buttonRegister)
        registerButton.setOnClickListener {
            saveUserCredentials()
        }
    }

    private fun saveUserCredentials() {
        val login = registerEditText.text.toString()
        val password = passwordEditText.text.toString()

        val userData = "$login:$password"

        try {
            val outputStreamWriter = OutputStreamWriter(resources.openRawResource(R.raw.users))
            outputStreamWriter.write(userData)
            outputStreamWriter.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}