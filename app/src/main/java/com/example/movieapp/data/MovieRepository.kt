package com.example.movieapp.data

import com.example.movieapp.retrofit.RetrofitInstance
import com.example.movieapp.retrofit.RetrofitService

class MovieRepository {

    private val retrofitService = RetrofitInstance.authService

    suspend fun getPopularMovies(apiKey: String) = retrofitService.getPopularMovies(apiKey)

    suspend fun getGenres(apiKey: String) = retrofitService.getGenres(apiKey)

    suspend fun getMovieDetails(movieId: Int, apiKey: String) = retrofitService.getMovieDetails(movieId, apiKey)

    suspend fun getMovieCredits(movieId: Int, apiKey: String) = retrofitService.getMovieCredits(movieId, apiKey)
}