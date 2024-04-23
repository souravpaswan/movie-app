package com.example.movieapp.ui.favouritesScreen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.R
import com.example.movieapp.database.FavouriteMovie
import com.example.movieapp.database.FavouriteMovieDb
import com.example.movieapp.database.FavouriteMovieRepository
import com.example.movieapp.databinding.FragmentFavouritesBinding
import com.example.movieapp.ui.homeScreen.MoviesListRVAdapter
import com.example.movieapp.ui.movieDetailsScreen.MovieCastRVAdapter

class FavouritesFragment : Fragment() {

    private lateinit var binding: FragmentFavouritesBinding
    private lateinit var favouriteMovieViewModel: FavouriteMovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourites, container, false)
        val dao = FavouriteMovieDb.getInstance(requireActivity()).favouriteMovieDao
        val favRepository = FavouriteMovieRepository(dao)
        val factory = FavouriteMovieViewModelFactory(favRepository)
        favouriteMovieViewModel = ViewModelProvider(requireActivity(), factory)[FavouriteMovieViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favouriteMovieViewModel.favourites.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                binding.favouriteMoviesRecyclerView.layoutManager =
                    LinearLayoutManager(requireContext())
                binding.favouriteMoviesRecyclerView.adapter = FavouriteMoviesRVAdapter(
                    it,
                    object : FavouriteMoviesRVAdapter.FavouriteMoviesRVAdapterClickListener{
                        override fun removeFavouriteOnClickListener(id: Int, name: String, release: String, url: String) {
                            val favouriteMovie = FavouriteMovie(id, name, release, url)
                            favouriteMovieViewModel.remove(favouriteMovie)
                            favouriteMovieViewModel.statusMessage.observe(viewLifecycleOwner, Observer {
                                Toast.makeText(requireContext(), favouriteMovieViewModel.statusMessage.value, Toast.LENGTH_SHORT).show()
                            })
                        }
                    })
            } else{
                Toast.makeText(requireContext(), "Emtpy", Toast.LENGTH_SHORT).show()
            }
        })
    }
}