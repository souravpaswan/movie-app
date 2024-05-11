package com.example.movieapp.ui.searchScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movieapp.R
import com.example.movieapp.data.MovieRepository
import com.example.movieapp.databinding.FragmentSearchBinding
import com.example.movieapp.model.SearchResponse
import com.example.movieapp.utils.APIConstants
import com.example.movieapp.viewmodel.MainViewModel
import com.example.movieapp.viewmodel.MainViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var searchResultsAdapter: SearchResultsRVAdapter

    internal class DebouncingQueryTextListener(
        lifecycle: Lifecycle,
        private val onDebouncingQueryTextChange: (String?) -> Unit
    ) : SearchView.OnQueryTextListener {
        var debouncePeriod: Long = 500

        private val coroutineScope = lifecycle.coroutineScope

        private var searchJob: Job? = null

        override fun onQueryTextSubmit(query: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            searchJob?.cancel()
            searchJob = coroutineScope.launch {
                newText?.let {
                    delay(debouncePeriod)
                    onDebouncingQueryTextChange(newText)
                }
            }
            return false
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        val repository = MovieRepository()
        val viewModelFactory = MainViewModelFactory(repository)
        mainViewModel = ViewModelProvider(requireActivity(), viewModelFactory)[MainViewModel::class.java]
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false
        }

        binding.searchResultRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        searchResultsAdapter = SearchResultsRVAdapter(
            emptyList(),
            object : SearchResultsRVAdapter.SearchResultItemOnClickListener {
                override fun showSearchItemMovieDetails(movieId: Int) {
                    mainViewModel.currentMovieId.value = movieId
                    findNavController().navigate(R.id.action_searchFragment_to_movieDetailsFragment2)
                }
            }
        )
        binding.searchResultRecyclerView.adapter = searchResultsAdapter

        mainViewModel.searchResult.observe(viewLifecycleOwner, Observer { searchResult ->
            lifecycleScope.launch {
                updateSearchResults(searchResult)
            }
        })

        lifecycleScope.launch {
            searchMovie()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(R.id.navigation_home)
        }
    }

    private suspend fun updateSearchResults(searchResult: SearchResponse?) {
        withContext(Dispatchers.Main) {
            if (searchResult != null && searchResult.results.isNotEmpty()) {
                binding.searchResultRecyclerView.visibility = View.VISIBLE
                binding.searchResultTextView.visibility = View.GONE
                searchResultsAdapter.searchResult = searchResult.results
                searchResultsAdapter.notifyDataSetChanged()
            } else {
                binding.searchResultTextView.visibility = View.VISIBLE
                binding.searchResultRecyclerView.visibility = View.GONE
            }
        }
    }

    suspend fun searchMovie(){
        binding.searchView.getQueryTextChangeStateFlow()
            .debounce(300)
            .filter { query ->
                if (query.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        binding.searchResultTextView.visibility = View.GONE
                        binding.searchResultRecyclerView.visibility = View.GONE
                    }
                    return@filter false
                } else {
                    return@filter true
                }
            }
            .distinctUntilChanged()
            .flatMapLatest { query ->
                dataFromNetwork(query)
                    .catch {
                        emitAll(flowOf(""))
                    }
            }
            .flowOn(Dispatchers.Default)
            .collect { result ->
                mainViewModel.getSearchResults(result, APIConstants.API_KEY)
                binding.searchResultRecyclerView.visibility = View.VISIBLE
            }
    }

    private fun SearchView.getQueryTextChangeStateFlow(): StateFlow<String> {

        val query = MutableStateFlow("")

        setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                query.value = newText
                return true
            }
        })
        return query
    }

    private fun dataFromNetwork(query: String): Flow<String> {
        return flow {
            delay(200)
            emit(query)
        }
    }
}