package com.example.movieapp

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.movieapp.data.MovieRepository
import com.example.movieapp.databinding.ActivityMainBinding
import com.example.movieapp.viewmodel.MainViewModel
import com.example.movieapp.viewmodel.MainViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(1000)
        installSplashScreen()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val repository = MovieRepository()
        val viewModelFactory = MainViewModelFactory(repository)
        mainViewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        mainViewModel.isGridView.value = false

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        navView.setupWithNavController(navController)
        setSupportActionBar(binding.toolbar)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.movieDetailsFragment2, R.id.searchFragment -> navView.visibility = View.GONE
                else -> navView.visibility = View.VISIBLE
            }
            when (destination.id) {
                R.id.navigation_home -> {
                    binding.toolbarRoot.toolbarText.text = getString(R.string.top_movies)
                    binding.toolbarRoot.listToggleImageView.visibility = View.VISIBLE
                    binding.toolbarRoot.searchImageView.visibility = View.VISIBLE
                }

                R.id.navigation_favourites -> {
                    binding.toolbarRoot.toolbarText.text = getString(R.string.favourites)
                    binding.toolbarRoot.listToggleImageView.visibility = View.INVISIBLE
                    binding.toolbarRoot.searchImageView.visibility = View.VISIBLE
                }

                R.id.navigation_settings -> {
                    binding.toolbarRoot.toolbarText.text = getString(R.string.settings)
                    binding.toolbarRoot.listToggleImageView.visibility = View.INVISIBLE
                    binding.toolbarRoot.searchImageView.visibility = View.VISIBLE
                }

                R.id.movieDetailsFragment2 -> {
                    binding.toolbarRoot.toolbarText.text = getString(R.string.movie_details)
                    binding.toolbarRoot.listToggleImageView.visibility = View.INVISIBLE
                    binding.toolbarRoot.searchImageView.visibility = View.INVISIBLE
                }

                R.id.searchFragment -> {
                    binding.toolbarRoot.toolbarText.text = getString(R.string.movie_search)
                    binding.toolbarRoot.listToggleImageView.visibility = View.INVISIBLE
                    binding.toolbarRoot.searchImageView.visibility = View.INVISIBLE
                }
            }
        }

        binding.toolbarRoot.searchImageView.setOnClickListener {
            navController.navigate(R.id.searchFragment)
        }

        binding.toolbarRoot.listToggleImageView.setOnClickListener {
            mainViewModel.isGridView.value = mainViewModel.isGridView.value?.not()
        }

        mainViewModel.isGridView.observe(this, Observer {
            val typedValue = TypedValue()
            val theme = binding.toolbarRoot.listToggleImageView.context.theme
            if(it){
                theme.resolveAttribute(R.attr.listToggleIconDrawable, typedValue, true)
            } else{
                theme.resolveAttribute(R.attr.gridToggleIconDrawable, typedValue, true)
            }
            val drawableResId = typedValue.resourceId
            binding.toolbarRoot.listToggleImageView.setImageResource(drawableResId)
        })
    }
}