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
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.blureagency.adapters.MovieImagesViewPagerAdapter
import com.example.movies.R
import com.example.movies.api.models.images.Logo
import com.example.movies.api.models.images.Poster
import com.example.movies.databinding.FragmentMovieDetailsBinding
import com.example.movies.room.MovieEntity
import com.example.movies.ui.MainActivity
import com.example.movies.ui.MoviesViewModel
import com.example.movies.utils.Resources
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {
     val moviesViewModel: MoviesViewModel by viewModels()

    lateinit var binding : FragmentMovieDetailsBinding
    private lateinit var movieImagesViewPagerAdapter: MovieImagesViewPagerAdapter




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieDetailsBinding.bind(view)

        showPR()



        // Getting Movie Id
        if (arguments != null){
     val movie = arguments?.getParcelable<MovieEntity>("movie")
    if (movie != null){
        moviesViewModel.getMovieDetails( movie.id)
        getMovieImages(movie.id)
    }else{
        Log.d("id","$id")
    }
}

        observeMovieDetails()


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


    private fun handleViewPager(list:List<Poster>){
        movieImagesViewPagerAdapter = MovieImagesViewPagerAdapter(list)
        binding.movieDetailsViewPager.adapter = movieImagesViewPagerAdapter
        binding.wormDotsIndicator.attachTo(binding.movieDetailsViewPager)
    }

    private fun observeMovieDetails(){
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

                        }

                    }
                }



            })
        }
    }




private fun getMovieImages(id:Int) {
    lifecycleScope.launch {
        moviesViewModel.getMovieImages(id)

        moviesViewModel.imageResponse.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resources.Error -> {
                    Toast.makeText(requireContext()," Error : Failed To Bring Movie Images  ",Toast.LENGTH_SHORT).show()
                }
                is Resources.Loading -> {

                }
                is Resources.Success ->{

                    it.data?.let {

                        handleViewPager(it.posters)

                    }

                }
            }
        })
    }
}

}
