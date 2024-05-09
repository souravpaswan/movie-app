package com.example.movieapp.ui.favouritesScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.database.FavouriteMovie
import com.example.movieapp.utils.APIConstants
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FavouriteMoviesRVAdapter(
    private val favourites: List<FavouriteMovie>,
    private val favouriteMoviesRVAdapterClickListener: FavouriteMoviesRVAdapterClickListener) :
    RecyclerView.Adapter<FavouriteMoviesRVAdapter.FavouriteMovieViewHolder>() {

    inner class FavouriteMovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val favouriteMovieTitleTextView: TextView = itemView.findViewById(R.id.favouriteMovieTitleTextView)
        val favouriteMovieImageView: ImageView = itemView.findViewById(R.id.favouriteMovieImageView)
        val favouriteMovieReleaseTextView: TextView = itemView.findViewById(R.id.favouriteMovieReleaseTextView)
        val removeFavouriteButton: FloatingActionButton = itemView.findViewById(R.id.removeFavouriteButton)
        val favListRVLayout: RelativeLayout = itemView.findViewById(R.id.favouriteListRelativeLayout)
    }

    interface FavouriteMoviesRVAdapterClickListener {
        fun removeFavouriteOnClickListener(id: Int, name: String, release: String, url: String)
        fun showFavouriteMovieDetailsOnClick(movieId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteMovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favourites_list, parent, false)
        return FavouriteMovieViewHolder(view)
    }

    override fun getItemCount(): Int {
        return favourites.size
    }

    override fun onBindViewHolder(holder: FavouriteMovieViewHolder, position: Int) {
        if (!favourites.isNullOrEmpty()) {
            holder.favouriteMovieTitleTextView.text = favourites[position].movieName
            holder.favouriteMovieReleaseTextView.text = favourites[position].movieRelease
            Glide.with(holder.favouriteMovieImageView)
                .load(favourites[position].imageUrl)
                .into(holder.favouriteMovieImageView)
            holder.removeFavouriteButton.setOnClickListener {
                favouriteMoviesRVAdapterClickListener.removeFavouriteOnClickListener(favourites[position].movieId,
                    favourites[position].movieName, favourites[position].movieRelease, favourites[position].imageUrl)
            }
            holder.favListRVLayout.setOnClickListener {
                favouriteMoviesRVAdapterClickListener.showFavouriteMovieDetailsOnClick(favourites[position].movieId)
            }
        } else {
            Toast.makeText(holder.itemView.context, "No movies found", Toast.LENGTH_SHORT).show()
        }
    }
}