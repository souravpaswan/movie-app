package com.example.movieapp.database

class FavouriteMovieRepository(private val dao: FavouriteMovieDao) {

    val favouriteMovies = dao.getAllFavouriteMovies()

    fun insert(favouriteMovie: FavouriteMovie){
        dao.insertFavouriteMovie(favouriteMovie)
    }

    fun delete(favouriteMovie: FavouriteMovie){
        dao.deleteFavouriteMovie(favouriteMovie)
    }

    fun deleteAll(){
        dao.deleteAll()
    }
}