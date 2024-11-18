package com.example.movies.ui.fragments

import android.os.Bundle
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UpComingFragment : Fragment(R.layout.fragment_up_coming) {
    val moviesViewModel: MoviesViewModel by viewModels()
    lateinit var binding: FragmentUpComingBinding
    lateinit var moviesAdapter : MovieRecyclerAdapter




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
           findNavController().navigate(R.id.action_upComingFragment_to_moviesFragment,bundle)
       }


        lifecycleScope.launch {
            moviesViewModel.upcomingMovies.collectLatest { topRatedMovies ->

                moviesAdapter.submitData(topRatedMovies)
            }
        }

    }

    private fun setUpRecycler(){
        moviesAdapter = MovieRecyclerAdapter()

        binding.recyclerUpcoming.apply {
            adapter = moviesAdapter
            layoutManager = GridLayoutManager(requireContext(),2)

        }
    }



    private fun hideProgressBar(){
        binding.paginationProgressBar.visibility = View.GONE

    }

    private fun showProgressBar(){

        binding.paginationProgressBar.visibility = View.VISIBLE

    }


}