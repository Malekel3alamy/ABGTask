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
import com.example.movies.databinding.FragmentUpComingBinding
import com.example.movies.ui.MainActivity
import com.example.movies.ui.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class UpComingFragment : Fragment(R.layout.fragment_up_coming) {
    val moviesViewModel: MoviesViewModel by viewModels()
    lateinit var binding: FragmentUpComingBinding
    lateinit var moviesAdapter : MovieRecyclerAdapter

    override fun onStart() {
        super.onStart()

        showProgressBar()
        observeUpComingMovies()

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUpComingBinding.bind(view)
        (activity as MainActivity).showToolbarAndNavigationView()
        setUpRecycler()

       moviesAdapter.onMovieClick={ movie ->

           val bundle = Bundle().apply {
               if (movie.id != null)
                   putParcelable("movie", movie)
           }
           findNavController().navigate(R.id.action_homeFragment_to_moviesFragment,bundle)
       }

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
    private fun setUpRecycler(){
        moviesAdapter = MovieRecyclerAdapter()

        binding.recyclerUpcoming.apply {
            adapter = moviesAdapter
            layoutManager = GridLayoutManager(requireContext(),2)
            itemAnimator?.endAnimations()
        }
    }



    private fun hideProgressBar(){
        binding.paginationProgressBar.visibility = View.GONE

    }

    private fun showProgressBar(){

        binding.paginationProgressBar.visibility = View.VISIBLE

    }


}