package com.robusta.photoweather.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.robusta.photoweather.R
import com.robusta.photoweather.data.response.WeatherResponse
import com.robusta.photoweather.databinding.ActivityMainBinding
import com.robusta.photoweather.getViewModel
import com.robusta.photoweather.setupWithNavController
import com.robusta.photoweather.utilities.LocationUtil
import com.robusta.photoweather.utilities.PermissionUtil
import com.robusta.photoweather.viewmodel.WeatherViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        setupBottomNavigationBar()
    }



    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)

        bottomNavigationView.itemIconTintList = null

        val navGraphIds = listOf(
            R.navigation.capture,
            R.navigation.history
        )

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this, Observer { navController ->
            //home_top_label.text = navController.currentDestination?.label
        })
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }
}