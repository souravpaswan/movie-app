package com.example.movieapp.dagger

import com.example.movieapp.data.MovieRepository
import dagger.Component

@Component
interface MovieRepositoryComponent {
    fun getMovieRepository(): MovieRepository
}