package com.example.cinemaapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class FavoriteFilmsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.favorite_films_activity)

        val allFilmsIcon = findViewById<ImageView>(R.id.allFilms)
        val cinemasIcon = findViewById<ImageView>(R.id.cinemasLocation)

        allFilmsIcon.setOnClickListener {
            val intent = Intent(this, AllFilmsActivity::class.java)
            startActivity(intent)
            finish()
        }

        cinemasIcon.setOnClickListener {
            val intent = Intent(this, CinemasMapActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
