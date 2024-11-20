package com.example.movies.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movies.R
import com.example.movies.adapter.MovieRecyclerAdapter
import com.example.movies.databinding.FragmentNowPlayingBinding
import com.example.movies.ui.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class NowPlayingFragment : Fragment(R.layout.fragment_now_playing) {
     val moviesViewModel: MoviesViewModel by viewModels()

    lateinit var binding: FragmentNowPlayingBinding
    private lateinit var nowPlayingMoviesAdapter : MovieRecyclerAdapter
    lateinit var ite_view_error : View

    override fun onStart() {
        super.onStart()

        showProgressBar()
        observeMovies()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNowPlayingBinding.bind(view)

        setUpRecycler()
        handleClickOnNowPlayingAdapter()



        // handle internet error
        ite_view_error = view.findViewById<View>(R.id.itemMoviesError)
        binding.itemMoviesError.retryButton.setOnClickListener {

            observeMovies()
            ite_view_error.visibility = View.GONE
        }

        if (!moviesViewModel.internetConnection(requireContext())){
            Toast.makeText(requireContext(),"Please Connect To Internet",Toast.LENGTH_SHORT).show()
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
     if (moviesViewModel.internetConnection(requireContext())){
         val bundle = Bundle().apply {
             if (movie.id != null)
                 putParcelable("movie",movie)
         }
         findNavController().navigate(R.id.action_homeFragment_to_moviesFragment,bundle)
     }else{
         Toast.makeText(requireContext(),"Please Connect To Internet",Toast.LENGTH_SHORT).show()
     }

        }
    }

private fun observeMovies(){

   this.lifecycleScope.launch(Dispatchers.IO) {
       moviesViewModel.nowPlayingMovies.collectLatest{nowPlayingMovies ->
           delay(2000L)
           withContext(Dispatchers.Main){
               hideProgressBar()
           }

           nowPlayingMoviesAdapter.submitData(nowPlayingMovies)

       }

   }



}







    var isError = false

    private fun hideProgressBar(){
        binding.paginationProgressBar.visibility = View.INVISIBLE

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