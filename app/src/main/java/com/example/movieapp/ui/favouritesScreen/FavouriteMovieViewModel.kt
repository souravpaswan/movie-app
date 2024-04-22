package com.example.movieapp.ui.favouritesScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
        repository.insert(favouriteMovie)
        viewModelScope.launch {
            favourites.value?.forEach { favouriteMovie ->
                Log.i("RoomDB", "Movie: ${favouriteMovie.movieName}, Release Date: ${favouriteMovie.movieRelease}")
            }
        }
    }
}