package com.example.movieapp.ui.homeScreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentHomeBinding
import com.example.movieapp.data.MovieRepository
import com.example.movieapp.database.FavouriteMovie
import com.example.movieapp.database.FavouriteMovieDao
import com.example.movieapp.database.FavouriteMovieDb
import com.example.movieapp.database.FavouriteMovieRepository
import com.example.movieapp.ui.favouritesScreen.FavouriteMovieViewModel
import com.example.movieapp.ui.favouritesScreen.FavouriteMovieViewModelFactory
import com.example.movieapp.utils.APIConstants
import com.example.movieapp.viewmodel.MainViewModel
import com.example.movieapp.viewmodel.MainViewModelFactory
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var favouriteMovieViewModel: FavouriteMovieViewModel
    private lateinit var favRepository: FavouriteMovieRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = this
        val repository = MovieRepository()
        val viewModelFactory = MainViewModelFactory(repository)
        mainViewModel = ViewModelProvider(requireActivity(), viewModelFactory)[MainViewModel::class.java]

        val dao = FavouriteMovieDb.getInstance(requireActivity()).favouriteMovieDao
        favRepository = FavouriteMovieRepository(dao)
        val factory = FavouriteMovieViewModelFactory(favRepository)
        favouriteMovieViewModel = ViewModelProvider(requireActivity(), factory)[FavouriteMovieViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayPopularMovies()
    }

    fun displayPopularMovies(){
        binding.progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            mainViewModel.getPopularMovies(APIConstants.API_KEY)
            mainViewModel.getGenres(APIConstants.API_KEY)
            mainViewModel.popularMoviesLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    binding.movieListRecyclerView.layoutManager =
                        LinearLayoutManager(requireContext())
                    binding.movieListRecyclerView.adapter = MoviesListRVAdapter(
                        it.results,
                        object : MoviesListRVAdapter.MovieListRVAdapterClickListener{
                            override fun fetchGenre(genreIds: List<Int>): String {
                                return displayGenres(genreIds)
                            }

                            override fun movieOnClickListener(movieId: Int) {
                                Log.i("Retrofit", "Movie id variable $movieId")
                                mainViewModel.currentMovieId.value = movieId
                                Log.i("Retrofit", "Movie id viewmodel ${mainViewModel.currentMovieId.value}")
                                findNavController().navigate(R.id.action_navigation_home_to_movieDetailsFragment2)
                            }

                            override fun addToFavouriteOnClickListener(holder: MoviesListRVAdapter.MovieViewHolder, movieId: Int, title: String, release: String, imageUrl: String) {
                                val favouriteMovie = FavouriteMovie(movieId, title, release, imageUrl)
                                lifecycleScope.launch {
                                    val existingMovie = favRepository.getMovieById(favouriteMovie.movieId)
                                    if (existingMovie != null) {
                                        favouriteMovieViewModel.remove(favouriteMovie)
                                        holder.addToFavouritesImageView.setImageResource(R.drawable.baseline_favorite_border_24)
                                        favouriteMovieViewModel.statusMessage.observe(viewLifecycleOwner, Observer {
                                            Toast.makeText(requireContext(), favouriteMovieViewModel.statusMessage.value, Toast.LENGTH_SHORT).show()
                                        })
                                    } else {
                                        favouriteMovieViewModel.insert(favouriteMovie)
                                        holder.addToFavouritesImageView.setImageResource(R.drawable.baseline_favorite_red_24)
                                        favouriteMovieViewModel.statusMessage.observe(viewLifecycleOwner, Observer {
                                            Toast.makeText(requireContext(), favouriteMovieViewModel.statusMessage.value, Toast.LENGTH_SHORT).show()
                                        })
                                    }
                                }
                            }

                            override fun addToFavouriteObserver(holder: MoviesListRVAdapter.MovieViewHolder, movieId: Int, title: String, release: String, imageUrl: String) {
                                val favouriteMovie = FavouriteMovie(movieId, title, release, imageUrl)
                                lifecycleScope.launch {
                                    val existingMovie = favRepository.getMovieById(favouriteMovie.movieId)
                                    if (existingMovie != null) {
                                        holder.addToFavouritesImageView.setImageResource(R.drawable.baseline_favorite_red_24)
                                    } else {
                                        holder.addToFavouritesImageView.setImageResource(R.drawable.baseline_favorite_border_24)

                                    }
                                }
                            }
                        })
                    binding.progressBar.visibility = View.GONE
                } else {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT)
                        .show()
                    Log.i("Retrofit", "HomeFragment else")
                }
            })
        }
    }

    fun displayGenres(genreIds: List<Int>) : String{
        var movieGenres = ""
        for(genreId in genreIds){
            mainViewModel.genreList.observe(this, Observer {
                it.genres.forEach{genre->
                    if(genreId == genre.id){
                        movieGenres += genre.name + ", "
                    }
                }
            })
        }
        return movieGenres.substring(0, movieGenres.length - 2)
    }
}