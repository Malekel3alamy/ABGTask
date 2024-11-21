package com.example.movies.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movies.R
import com.example.movies.adapter.MovieRecyclerAdapter
import com.example.movies.databinding.FragmentBaseBinding
import com.example.movies.ui.MoviesViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseFragment : Fragment(R.layout.fragment_base) {
    val moviesViewModel: MoviesViewModel by viewModels()

    lateinit var binding: FragmentBaseBinding
    lateinit var moviesAdapter: MovieRecyclerAdapter



    fun setUpRecycler() {
        moviesAdapter = MovieRecyclerAdapter()

        binding.recyclerNowPlaying.apply {
            adapter = moviesAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    fun handleClickOnMovieAdapter(actionId: Int) {
        moviesAdapter.onMovieClick = { movie ->
            if (moviesViewModel.internetConnection(requireContext())) {
                val bundle = Bundle().apply {
                    if (movie.id != null)
                        putParcelable("movie", movie)
                }

                findNavController().navigate(actionId, bundle)
            } else {
                Toast.makeText(requireContext(), "Please Connect To Internet", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }


    // check internet if not available show toast message to user
    fun checkInternet() {
        if (!moviesViewModel.internetConnection(requireContext())) {
            Toast.makeText(requireContext(), "Please Connect To Internet", Toast.LENGTH_SHORT)
                .show()
        }
    }


    // var isError = false

    fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE

    }

    fun showProgressBar() {

        binding.paginationProgressBar.visibility = View.VISIBLE

    }





}


