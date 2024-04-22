package com.example.movieapp.ui.favouritesScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movieapp.database.FavouriteMovieRepository
import com.example.movieapp.viewmodel.MainViewModel

class FavouriteMovieViewModelFactory(private val repository: FavouriteMovieRepository):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FavouriteMovieViewModel::class.java)){
            return FavouriteMovieViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }

}