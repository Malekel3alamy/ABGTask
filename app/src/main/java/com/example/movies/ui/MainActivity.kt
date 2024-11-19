package com.example.movies.ui


import android.os.Bundle
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController

import androidx.navigation.fragment.NavHostFragment

import androidx.navigation.ui.setupWithNavController

import com.example.movies.R
import com.example.movies.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
     lateinit var navController: NavController




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showToolbarAndNavigationView()



        // NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

    }

 fun hideToolbarAndNavigationView(){
     binding.bottomNavigationView.visibility = View.GONE

 }

    fun showToolbarAndNavigationView(){
        binding.bottomNavigationView.visibility = View.VISIBLE
    }



}