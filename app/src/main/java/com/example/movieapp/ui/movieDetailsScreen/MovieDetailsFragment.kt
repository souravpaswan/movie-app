package com.example.movieapp.ui.movieDetailsScreen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.data.MovieRepository
import com.example.movieapp.databinding.FragmentMovieDetailsBinding
import com.example.movieapp.model.Genre
import com.example.movieapp.utils.APIConstants
import com.example.movieapp.viewmodel.MainViewModel
import com.example.movieapp.viewmodel.MainViewModelFactory
import kotlinx.coroutines.launch

class MovieDetailsFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailsBinding
    private lateinit var mainViewModel: MainViewModel
    private val trailerPaths = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_details, container, false)
        val repository = MovieRepository()
        val viewModelFactory = MainViewModelFactory(repository)
        mainViewModel = ViewModelProvider(requireActivity(), viewModelFactory)[MainViewModel::class.java]
        binding.myViewModel = mainViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.progressBar2.visibility = View.VISIBLE
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            mainViewModel.getMovieDetails(mainViewModel.currentMovieId.value ?: 0, APIConstants.API_KEY)
        }
        mainViewModel.movieDetails.observe(viewLifecycleOwner, Observer {
            it.poster_path?.let { posterPath ->
                val imageUrl = APIConstants.IMAGE_PATH + posterPath
                Glide.with(binding.movieDetailsPosterImageView)
                    .load(imageUrl)
                    .into(binding.movieDetailsPosterImageView)
                Log.i("Retrofit", "Poster url = $imageUrl")
            }
            it.backdrop_path?.let{ backdropPath ->
                val imageUrl = APIConstants.IMAGE_PATH + backdropPath
                Glide.with(binding.trailerImageView)
                    .load(imageUrl)
                    .into(binding.trailerImageView)
                Log.i("Retrofit", "Backgrop url = $imageUrl")
            }
            binding.movieDetailsRatingTextView.text = "Rating: " + it.vote_average.toString().substring(0,3)
            binding.movieDetailsGenreTextView.text = "Genres: " + displayGenres(it.genres)
            checkDataLoaded()
        })
        getCastDetails()
        getVideoDetails()
    }

    fun getCastDetails(){
        lifecycleScope.launch {
            mainViewModel.getMovieCredits(mainViewModel.currentMovieId.value!!, APIConstants.API_KEY)
            mainViewModel.creditDetails.observe(viewLifecycleOwner, Observer {
                if(it != null){
                    binding.castMembersRecyclerView.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

                    binding.castMembersRecyclerView.adapter = MovieCastRVAdapter(it.cast)
                }
                checkDataLoaded()
            })
        }
    }

    private fun getVideoDetails() {
        lifecycleScope.launch {
            mainViewModel.getVideoDetails(mainViewModel.currentMovieId.value!!, APIConstants.API_KEY)
            mainViewModel.videoDetails.observe(viewLifecycleOwner, Observer {
                trailerPaths.clear()
                for(x in it.results){
                    if(x.type.equals("Trailer", ignoreCase = true)) {
                        trailerPaths.add(x.key)
                    }
                }
                Log.i("Retrofit", "Video key $trailerPaths")
                if(!trailerPaths.isNullOrEmpty()){
                    binding.trailersRecycleView.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

                    binding.trailersRecycleView.adapter = TrailersRVAdapter(trailerPaths)
                }
                checkDataLoaded()
            })
        }
    }

    fun displayGenres(genres: List<Genre>) : String {
        var movieGenres = ""
        for(genre in genres){
            movieGenres += genre.name + ", "
        }
        return movieGenres.substring(0, movieGenres.length - 2)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Glide.with(this).clear(binding.movieDetailsPosterImageView)
    }

    private fun checkDataLoaded() {
        if (mainViewModel.movieDetails.value != null &&
            mainViewModel.creditDetails.value != null &&
            mainViewModel.videoDetails.value != null) {
            binding.progressBar2.visibility = View.GONE
        }
    }
}