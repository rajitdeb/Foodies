package com.rajit.foodies.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.rajit.foodies.R
import com.rajit.foodies.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // NavController -> Nav Host Fragment Basically
        navController = findNavController(R.id.navHostFragment)

        // AppBar or Toolbar Setup and for navigation
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.recipesFragment,
                R.id.favoritesFragment,
                R.id.foodJokeFragment
            )
        )

        // setting bottomNavigationView
        binding.bottomNavigationView.setupWithNavController(navController)

        //setting ActionBar to work with NavController
        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    // For the up button to work
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}