package com.example.cinemaapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class AccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_layout)

        val allFilmsIcon = findViewById<ImageView>(R.id.allFilms)
        val favoriteFilmsIcon = findViewById<ImageView>(R.id.favoriteFilms)

        allFilmsIcon.setOnClickListener {
            val intent = Intent(this, AllFilmsActivity::class.java)
            startActivity(intent)
            finish()
        }

        favoriteFilmsIcon.setOnClickListener {
            val intent = Intent(this, FavoriteFilmsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
