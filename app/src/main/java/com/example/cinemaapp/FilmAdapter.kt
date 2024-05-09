package com.example.cinemaapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val imageViewPoster: ImageView = itemView.findViewById(R.id.imageViewPoster)
    val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
}

class FilmAdapter : RecyclerView.Adapter<FilmViewHolder>() {
    private var films: List<Film> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.film_item_layout, parent, false)
        return FilmViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val film = films[position]
        val imageId = holder.itemView.context.resources.getIdentifier(film.picture, "drawable", holder.itemView.context.packageName)
        holder.imageViewPoster.setImageResource(imageId)
    }

    override fun getItemCount(): Int {
        return films.size
    }

    fun setFilms(films: List<Film>) {
        this.films = films
        notifyDataSetChanged()
    }
}
