package com.example.movies.repo

import androidx.lifecycle.LiveData
import com.example.movies.api.models.Movie
import com.example.movies.api.models.MovieResponse
import com.example.movies.api.models.details.DetailsResponse
import com.example.movies.api.models.images.ImagesResponse
import com.example.movies.room.MovieEntity
import com.example.movies.utils.Resources

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
    suspend fun  upsertAllMovies(movies:List<MovieEntity>)

    // Delete All Room Database
    suspend fun deleteAll()

    // Get All Data From Room
    suspend   fun getAllData(): LiveData<List<MovieEntity>>


    suspend fun getMovieImages(id:Int) :Resources<ImagesResponse>





}