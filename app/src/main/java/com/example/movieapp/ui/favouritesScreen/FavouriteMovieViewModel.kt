package com.example.movieapp.ui.favouritesScreen

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.movieapp.database.FavouriteMovie
import com.example.movieapp.database.FavouriteMovieRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FavouriteMovieViewModel(private val repository: FavouriteMovieRepository): ViewModel() {

    val favourites = repository.favouriteMovies

    private val _movieTitle = MutableLiveData<String>()
    val movieTitle: LiveData<String> get() = _movieTitle

    private val _release = MutableLiveData<String>()
    val release: LiveData<String> get() = _release

    fun insert(favouriteMovie: FavouriteMovie) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val existingMovie = repository.getMovieById(favouriteMovie.movieId)
            if (existingMovie != null) {
                Log.i("RoomDB", "Movie with id ${favouriteMovie.movieId} already exists in the database.")
            } else {
                repository.insert(favouriteMovie)
            }
        } catch (e: Exception) {
            Log.e("RoomDB", "Failed to insert movie", e)
        }
    }
}