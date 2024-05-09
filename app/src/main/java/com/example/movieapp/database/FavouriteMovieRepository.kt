package com.example.movieapp.database

class FavouriteMovieRepository(private val dao: FavouriteMovieDao) {

    val favouriteMovies = dao.getAllFavouriteMovies()

    fun insert(favouriteMovie: FavouriteMovie){
        dao.insertFavouriteMovie(favouriteMovie)
    }

    suspend fun getMovieById(movieId: Int): FavouriteMovie? {
        return dao.getMovieById(movieId)
    }

    fun delete(favouriteMovie: FavouriteMovie){
        dao.deleteFavouriteMovie(favouriteMovie)
    }

    fun deleteAll(){
        dao.deleteAll()
    }
}