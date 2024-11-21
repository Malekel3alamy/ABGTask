package com.example.movies.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movies.R
import com.example.movies.adapter.MovieRecyclerAdapter
import com.example.movies.databinding.FragmentBaseBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class TopRatedFragment : BaseFragment() {

    override fun onStart() {
        super.onStart()

        showProgressBar()

        checkInternet()


        observeTopRatedMovies()



    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBaseBinding.bind(view)

        setUpRecycler()

        handleClickOnMovieAdapter(R.id.action_homeFragment_to_moviesFragment)


    }


    private fun observeTopRatedMovies(){
        this.lifecycleScope.launch {
            moviesViewModel.topRatedMovies.collectLatest { topRatedMovies ->
                delay(2000L)
                withContext(Dispatchers.Main){
                    hideProgressBar()
                }
                moviesAdapter.submitData(topRatedMovies)
            }
        }
    }




}