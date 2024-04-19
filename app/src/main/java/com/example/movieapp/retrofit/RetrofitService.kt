package com.example.movieapp.retrofit

import com.example.movieapp.model.GenreResponse
import com.example.movieapp.model.MovieDetails
import com.example.movieapp.model.MovieList
import com.example.movieapp.utils.APIConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitService {

    @GET(APIConstants.POPULAR_MOVIES)
    suspend fun getPopularMovies(@Query("api_key")apiKey : String): Response<MovieList>

    @GET(APIConstants.GENRE_MOVIE_LIST)
    suspend fun getGenres(@Query("api_key")apiKey : String): Response<GenreResponse>

    @GET(APIConstants.MOVIE_DETAILS)
    suspend fun getMovieDetails(@Path("movie_id") movieId: Int, @Query("api_key")apiKey: String): Response<MovieDetails>
}