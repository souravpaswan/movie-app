package com.example.movieapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_movies_table")
data class FavouriteMovie(
    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    val movieId : Int,

    @ColumnInfo(name = "movie_name")
    val movieName : String,

    @ColumnInfo(name = "movie_release")
    val movieRelease : String,

    @ColumnInfo(name = "image_url")
    val imageUrl: String
)