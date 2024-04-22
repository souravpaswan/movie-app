package com.example.movieapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavouriteMovie::class],version = 1)
abstract class FavouriteMovieDb : RoomDatabase() {

    abstract val favouriteMovieDao : FavouriteMovieDao

    companion object{
        @Volatile
        private var INSTANCE: FavouriteMovieDb? = null
        fun getInstance(context: Context):FavouriteMovieDb{
            synchronized(this){
                var instance = INSTANCE
                if(instance==null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FavouriteMovieDb::class.java,
                        "favourite_movies_database"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}