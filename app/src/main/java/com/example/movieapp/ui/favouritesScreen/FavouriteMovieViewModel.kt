package com.example.movieapp.ui.favouritesScreen

import android.util.Log
import android.widget.Toast
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

    private val _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String> get() = _statusMessage

    private val _favouriteMovieDetails = MutableLiveData<FavouriteMovie>()
    val favouriteMovieDetails: LiveData<FavouriteMovie> get() = _favouriteMovieDetails

    fun insert(favouriteMovie: FavouriteMovie) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val existingMovie = repository.getMovieById(favouriteMovie.movieId)
            if (existingMovie != null) {
                Log.i("RoomDB", "Movie with id ${favouriteMovie.movieId} already exists in the database.")
            } else {
                repository.insert(favouriteMovie)
                _favouriteMovieDetails.postValue(favouriteMovie)
            }
        } catch (e: Exception) {
            Log.e("RoomDB", "Failed to insert movie", e)
            _statusMessage.postValue("Something went wrong!")
        }
    }

    fun remove(favouriteMovie: FavouriteMovie) = viewModelScope.launch(Dispatchers.IO) {
        try{
            repository.delete(favouriteMovie)
            _statusMessage.postValue("Removed from favourites")
        } catch(e: Exception){
            _statusMessage.postValue("Something went wrong!")
        }
    }
}