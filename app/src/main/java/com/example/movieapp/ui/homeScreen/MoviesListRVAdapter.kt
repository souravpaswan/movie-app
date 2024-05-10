package com.example.movieapp.ui.homeScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.model.Movie
import com.example.movieapp.utils.APIConstants
import com.example.movieapp.viewmodel.MainViewModel
import okhttp3.internal.wait

class MoviesListRVAdapter(
    private val movies: List<Movie>,
    private val mainViewModel: MainViewModel,
    private val movieListRVAdapterClickListener: MovieListRVAdapterClickListener
) :
    RecyclerView.Adapter<MoviesListRVAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var movieTitleTextView: TextView? = null
        var moviePosterImageView: ImageView? = null
        var movieReleaseDateTextView: TextView? = null
        var movieRatingTextView: TextView? = null
        var movieGenreTextView: TextView? = null
        var addToFavouritesImageView: ImageView? = null
        var listRVLayout: RelativeLayout? = null
        var gridRVLayout: RelativeLayout? = null

        var gridTitleTextView: TextView? = null
        var gridPosterImageView: ImageView? = null
        var gridReleaseDateTextView: TextView? = null
        var gridRatingTextView: TextView? = null
        var gridGenreTextView: TextView? = null
        var gridAddToFavouritesImageView: ImageView? = null

        init {
            if(mainViewModel.isGridView.value == true){
                gridTitleTextView = itemView.findViewById(R.id.gridTitleTextView)
                gridPosterImageView = itemView.findViewById(R.id.gridPosterImageView)
                gridReleaseDateTextView = itemView.findViewById(R.id.gridReleaseDateTextView)
                gridRatingTextView = itemView.findViewById(R.id.gridRatingTextView)
                gridGenreTextView = itemView.findViewById(R.id.gridGenreTextView)
                gridAddToFavouritesImageView = itemView.findViewById(R.id.gridAddToFavouritesImageView)
                gridRVLayout = itemView.findViewById(R.id.moviesGridLinearLayout)

            } else{
                movieTitleTextView = itemView.findViewById(R.id.movieTitleTextView)
                moviePosterImageView= itemView.findViewById(R.id.moviePosterImageView)
                movieReleaseDateTextView = itemView.findViewById(R.id.movieReleaseDateTextView)
                movieRatingTextView = itemView.findViewById(R.id.movieRatingTextView)
                movieGenreTextView = itemView.findViewById(R.id.movieGenreTextView)
                addToFavouritesImageView = itemView.findViewById(R.id.addToFavouritesImageView)
                listRVLayout = itemView.findViewById(R.id.moviesListRelativeLayout)
            }
        }
    }

    interface MovieListRVAdapterClickListener {
        fun fetchGenre(genreIds: List<Int>): String
        fun movieOnClickListener(movieId: Int)
        fun addToFavouriteOnClickListener(holder: MovieViewHolder, movieId: Int, title: String, release: String, imageUrl: String)
        fun addToFavouriteObserver(holder: MovieViewHolder, movieId: Int, title: String, release: String, imageUrl: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = if(mainViewModel.isGridView.value == true) {
            LayoutInflater.from(parent.context).inflate(R.layout.movies_grid, parent, false)
        } else {
            LayoutInflater.from(parent.context).inflate(R.layout.movies_list, parent, false)
        }
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        if (!movies.isNullOrEmpty()) {
            val imageUrl = APIConstants.IMAGE_PATH + movies[position].poster_path
            val genres = movieListRVAdapterClickListener.fetchGenre(movies[position].genre_ids)
            if(mainViewModel.isGridView.value == true){
                holder.gridTitleTextView?.text = movies[position].title
                holder.gridReleaseDateTextView?.text = movies[position].release_date
                holder.gridRatingTextView?.text =
                    movies[position].vote_average.toString().substring(0, 3)

                holder.gridGenreTextView?.text = genres
                Glide.with(holder.gridPosterImageView!!)
                    .load(imageUrl)
                    .into(holder.gridPosterImageView!!)
                movieListRVAdapterClickListener.addToFavouriteObserver(holder, movies[position].id, movies[position].title, movies[position].release_date, imageUrl)
                holder.gridRVLayout?.setOnClickListener {
                    movieListRVAdapterClickListener.movieOnClickListener(movies[position].id)
                }
                holder.gridAddToFavouritesImageView?.setOnClickListener {
                    movieListRVAdapterClickListener.addToFavouriteOnClickListener(holder, movies[position].id, movies[position].title, movies[position].release_date, imageUrl)
                }

            } else {
                holder.movieTitleTextView?.text = movies[position].title
                holder.movieReleaseDateTextView?.text = movies[position].release_date
                holder.movieRatingTextView?.text =
                    movies[position].vote_average.toString().substring(0, 3)
                holder.movieGenreTextView?.text = genres
                Glide.with(holder.moviePosterImageView!!)
                    .load(imageUrl)
                    .into(holder.moviePosterImageView!!)
                movieListRVAdapterClickListener.addToFavouriteObserver(holder, movies[position].id, movies[position].title, movies[position].release_date, imageUrl)
                holder.listRVLayout?.setOnClickListener {
                    movieListRVAdapterClickListener.movieOnClickListener(movies[position].id)
                }
                holder.addToFavouritesImageView?.setOnClickListener {
                    movieListRVAdapterClickListener.addToFavouriteOnClickListener(holder, movies[position].id, movies[position].title, movies[position].release_date, imageUrl)
                }
            }
        } else {
            Toast.makeText(holder.itemView.context, "No movies found", Toast.LENGTH_SHORT).show()
        }
    }
}