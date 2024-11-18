package com.example.movies.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies.R
import com.example.movies.adapter.MovieRecyclerAdapter
import com.example.movies.adapter.NowPlayingMoviesAdapter
import com.example.movies.adapter.TopRatedMoviesAdapter
import com.example.movies.adapter.UpComingMoviesAdapter
import com.example.movies.databinding.FragmentNowPlayingBinding
import com.example.movies.ui.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class NowPlayingFragment : Fragment(R.layout.fragment_now_playing) {
     val moviesViewModel: MoviesViewModel by viewModels()
    lateinit var binding: FragmentNowPlayingBinding
    private lateinit var nowPlayingMoviesAdapter : MovieRecyclerAdapter
    lateinit var ite_view_error : View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNowPlayingBinding.bind(view)

        showProgressBar()
        setUpRecycler()
        handleClickOnNowPlayingAdapter()


        checkInternetThenObserveMovies()

        ite_view_error = view.findViewById<View>(R.id.itemMoviesError)
        binding.itemMoviesError.retryButton.setOnClickListener {

            checkInternetThenObserveMovies()
            ite_view_error.visibility = View.GONE
        }

    }

    private fun setUpRecycler(){
        nowPlayingMoviesAdapter = MovieRecyclerAdapter()

        binding.recyclerNowPlaying.apply {
            adapter = nowPlayingMoviesAdapter
           layoutManager = GridLayoutManager(requireContext(),2)
        }
    }

    private fun handleClickOnNowPlayingAdapter(){
        nowPlayingMoviesAdapter.onMovieClick= { movie ->

            val bundle = Bundle().apply {
                if (movie.id != null)
                    putParcelable("movie",movie)
            }
            findNavController().navigate(R.id.action_nowPlayingFragment_to_moviesFragment,bundle)
        }
    }

private fun checkInternetThenObserveMovies(){
    if (moviesViewModel.internetConnection(requireContext())){

        lifecycleScope.launch(Dispatchers.IO) {

            moviesViewModel.nowPlayingMovies.collectLatest{nowPlayingMovies ->
                nowPlayingMoviesAdapter.submitData(nowPlayingMovies)

               withContext(Dispatchers.Main){
                   hideProgressBar()
               }
            }
        }


    }else{
        lifecycleScope.launch(Dispatchers.Main) {
            ite_view_error.visibility = View.VISIBLE

        }

    }
}







    var isError = false

    private fun hideProgressBar(){
        binding.paginationProgressBar.visibility = View.GONE

    }

    private fun showProgressBar(){

        binding.paginationProgressBar.visibility = View.VISIBLE

    }
    private fun hideErrorMessage(){
        binding.itemMoviesError.errorText.visibility = View.INVISIBLE
        binding.itemMoviesError.retryButton.visibility=View.INVISIBLE
        isError = false
    }
    private fun showErrorMessage(message: String){
         binding.itemMoviesError.errorText.visibility=View.VISIBLE
        binding.itemMoviesError.retryButton.visibility=View.VISIBLE
        isError = true
        binding.itemMoviesError.errorText.text = message
    }


}