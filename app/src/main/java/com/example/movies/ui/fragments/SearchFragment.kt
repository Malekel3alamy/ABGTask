package com.example.movies.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies.R
import com.example.movies.adapter.MovieRecyclerAdapter
import com.example.movies.adapter.SearchAdapter
import com.example.movies.databinding.FragmentSearchBinding
import com.example.movies.room.toMovieEntity
import com.example.movies.ui.MainActivity
import com.example.movies.ui.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.internal.notifyAll

@AndroidEntryPoint
class SearchFragment() : Fragment(R.layout.fragment_search) {
   private lateinit var binding : FragmentSearchBinding

    private val moviesViewModel: MoviesViewModel by viewModels()
    private lateinit var moviesAdapter : SearchAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)


        setUpRecycler()

        // handle user click on movie item
        moviesAdapter.onMovieClick={ movie ->

            val bundle = Bundle().apply {

                if (movie.id != null)
                    putParcelable("movie",movie.toMovieEntity(""))
            }
            findNavController().navigate(R.id.action_searchFragment_to_moviesFragment,bundle)
        }
        binding.backBtnSearch.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.movieEditText.addTextChangedListener(object  : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

                binding.searchIcon.setOnClickListener {
                    showPR()
                    s?.let {
                        if (s.toString().isNotEmpty()) {
                            moviesViewModel.searchMovie(s.toString())
                            if (moviesViewModel.searchMovies != null) {
                                lifecycleScope.launch {

                                    moviesViewModel.searchMovies!!.collect {
                                        hidePR()
                                        moviesAdapter.submitData(it)
                                        Log.d("SearchResult", "search not empty")
                                    }
                                }

                            } else {
                                Toast.makeText(requireContext()," Sorry Could not Find This Movie",
                                    Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        })
    }
    private fun setUpRecycler(){
        moviesAdapter = SearchAdapter()

        binding.rvSearch.apply {
            adapter = moviesAdapter
            layoutManager = GridLayoutManager(requireContext(),2)

        }
    }

    fun showPR(){
        binding.searchPR.visibility = View.VISIBLE
    }

    fun hidePR(){
        binding.searchPR.visibility = View.GONE
    }
}