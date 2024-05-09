package com.example.movieapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavouriteMovieDao {
    @Insert
    fun insertFavouriteMovie(favouriteMovie: FavouriteMovie)

    @Delete
    fun deleteFavouriteMovie(favouriteMovie: FavouriteMovie)

    @Query("DELETE FROM favourite_movies_table")
    fun deleteAll()

    @Query("SELECT * FROM favourite_movies_table")
    fun getAllFavouriteMovies(): LiveData<List<FavouriteMovie>>

    @Query("SELECT * FROM favourite_movies_table WHERE movie_id = :movieId LIMIT 1")
    suspend fun getMovieById(movieId: Int): FavouriteMovie?
}