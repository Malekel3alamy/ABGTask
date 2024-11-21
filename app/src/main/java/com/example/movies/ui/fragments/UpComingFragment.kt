package com.example.movies.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.movies.R
import com.example.movies.databinding.FragmentBaseBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class UpComingFragment : BaseFragment() {


    override fun onStart() {
        super.onStart()

        showProgressBar()
        observeUpComingMovies()

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBaseBinding.bind(view)

        setUpRecycler()


        checkInternet()
         handleClickOnMovieAdapter(R.id.action_homeFragment_to_moviesFragment)


    }

    private fun observeUpComingMovies(){
        this.lifecycleScope.launch {
            moviesViewModel.upcomingMovies.collectLatest { upcomingMovies ->
                delay(2000L)
                withContext(Dispatchers.Main){

                    hideProgressBar()
                }
                moviesAdapter.submitData(upcomingMovies)
            }
        }
    }


}