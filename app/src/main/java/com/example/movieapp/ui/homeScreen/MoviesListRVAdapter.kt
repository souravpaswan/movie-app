package com.example.movieapp.ui.homeScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.model.Movie

class MoviesListRVAdapter(
    private val movies: List<Movie>,
    private val movieListRVAdapterClickListener: MovieListRVAdapterClickListener
) :
    RecyclerView.Adapter<MoviesListRVAdapter.MovieViewHolder>(){

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val movieTitleTextView: TextView = itemView.findViewById(R.id.movieTitleTextView)
        val moviePosterImageView: ImageView = itemView.findViewById(R.id.moviePosterImageView)
        val movieReleaseDateTextView: TextView = itemView.findViewById(R.id.movieReleaseDateTextView)
        val movieRatingTextView: TextView = itemView.findViewById(R.id.movieRatingTextView)
        val movieGenreTextView: TextView = itemView.findViewById(R.id.movieGenreTextView)
    }

    interface MovieListRVAdapterClickListener{
        fun fetchGenre(genreIds: List<Int>) : String
        fun movieOnClickListener(movieId : Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movies_list, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        if(!movies.isNullOrEmpty()){
            holder.movieTitleTextView.text = movies[position].title
            holder.movieReleaseDateTextView.text = movies[position].release_date
            holder.movieRatingTextView.text = movies[position].vote_average.toString().substring(0,3)
            val genres = movieListRVAdapterClickListener.fetchGenre(movies[position].genre_ids)
            holder.movieGenreTextView.text = genres
            val imageUrl = "https://image.tmdb.org/t/p/w500${movies[position].poster_path}"
            Glide.with(holder.moviePosterImageView)
                .load(imageUrl)
                .into(holder.moviePosterImageView)
            holder.moviePosterImageView.setOnClickListener {
                movieListRVAdapterClickListener.movieOnClickListener(movies[position].id)
            }
        } else{
            Toast.makeText(holder.itemView.context, "No movies found", Toast.LENGTH_SHORT).show()
        }
    }
}