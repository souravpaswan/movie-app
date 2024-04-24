package com.example.movieapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.movieapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

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
                R.id.navigation_home -> binding.toolbarRoot.toolbarText.text =
                    getString(R.string.top_movies)

                R.id.navigation_favourites -> binding.toolbarRoot.toolbarText.text =
                    getString(R.string.favourites)

                R.id.navigation_settings -> binding.toolbarRoot.toolbarText.text =
                    getString(R.string.settings)

                R.id.movieDetailsFragment2 -> binding.toolbarRoot.toolbarText.text =
                    getString(R.string.movie_details)

                R.id.searchFragment -> binding.toolbarRoot.toolbarText.text =
                    getString(R.string.movie_search)
            }
        }

        binding.toolbarRoot.searchImageView.setOnClickListener {
            navController.navigate(R.id.searchFragment)
        }
    }
}