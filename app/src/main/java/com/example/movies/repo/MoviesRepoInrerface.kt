package com.example.movies.repo

import androidx.lifecycle.LiveData
import com.example.movies.api.RetrofitInstance
import com.example.movies.models.Movie
import com.example.movies.models.MovieResponse
import com.example.movies.models.details.DetailsResponse
import com.example.movies.utils.Resources
import retrofit2.Response

interface MoviesRepoInrerface {

    suspend fun getTopRatedMovies(pageNumber:Int) : Resources<MovieResponse>

    suspend fun getPopularMovies(pageNumber:Int): Resources<MovieResponse>

    suspend fun getNowPlayingMovies(pageNumber:Int): Resources<MovieResponse>
    suspend fun getUpcomingMovies(pageNumber:Int): Resources<MovieResponse>


    // Search For Movies
    suspend fun  search(keyWords: String,pageNumber:Int): Resources<MovieResponse>
    // Get Details
    suspend fun  getDetails(movie_id:Int): Resources<DetailsResponse>

    // Insert Data To Room
    suspend fun  upsert(movies:Movie)

    // Delete All Room Database
    suspend fun deleteAll()

    // Get All Data From Room
    suspend   fun getAllData(): LiveData<List<Movie>>

    // Delete One Movie

    suspend fun deleteMovie(movies:Movie)

    suspend fun getMovie(id:Int) :Movie



}