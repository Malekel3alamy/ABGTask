package com.example.movies.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.movies.R
import com.example.movies.databinding.FragmentMovieDetailsBinding
import com.example.movies.api.models.Movie
import com.example.movies.ui.MainActivity
import com.example.movies.ui.MoviesViewModel
import com.example.movies.utils.Resources
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {
     val moviesViewModel: MoviesViewModel by viewModels()
    lateinit var binding : FragmentMovieDetailsBinding

    var movie : Movie? = null




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieDetailsBinding.bind(view)

        showPR()

        // Getting Movie Id
        if (arguments!= null){
            (activity as MainActivity).hideToolbarAndNavigationView()
     movie = arguments?.getParcelable<Movie>("movie")
    if (movie!!.id != null){
        moviesViewModel.getMovieDetails(movie!!.id!!)
    }else{
        Log.d("id","$id")
    }
}
        lifecycleScope.launch {
            moviesViewModel.detailsResponse.observe(viewLifecycleOwner, Observer {
               when(it){
                   is Resources.Error -> {
                       hidePR()
                       Toast.makeText(requireContext()," Error : Failed To Bring Movie Details  ",Toast.LENGTH_SHORT).show()
                   }
                   is Resources.Loading -> {
                       showPR()
                   }
                   is Resources.Success ->{
                       hidePR()
                       it.data.let {
                           binding.detailsTitle.text = it?.title
                           binding.detailsOverview.text=it?.overview
                           binding.movieDetailsFragmentMovieVote.text = "Vote : ${it?.vote_average}"
                           Log.d("Language",it?.original_language.toString())
                           Glide.with(view).load("https://image.tmdb.org/t/p/w500/${it?.poster_path}").into(binding.moviesMovieImage)
                       }

                   }
               }



            })
        }

        // handle click on back button
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

    }



    fun showPR(){
        binding.moviePR.visibility = View.VISIBLE
        binding.movieFragment.visibility= View.INVISIBLE
    }
    fun hidePR(){
        binding.moviePR.visibility = View.GONE
        binding.movieFragment.visibility= View.VISIBLE
    }



}
