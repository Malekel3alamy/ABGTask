package com.example.movies.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movies.api.models.Movie
import com.example.movies.api.models.details.DetailsResponse
import com.example.movies.paging.NowPlayingMoviesRemoteMediator
import com.example.movies.paging.PopularMoviesRemoteMediator
import com.example.movies.paging.SearchMoviesPagingSource
import com.example.movies.paging.TopRatedMoviesRemoteMediator
import com.example.movies.paging.UpComingMoviesRemoteMediator
import com.example.movies.repo.MoviesRepoInrerface
import com.example.movies.room.MovieEntity
import com.example.movies.room.MoviesDatabase
import com.example.movies.utils.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor( private val moviesRepo:  MoviesRepoInrerface,private val moviesDatabase: MoviesDatabase) : ViewModel() {

    var detailsResponse =  MutableLiveData<Resources<DetailsResponse>>()


    var searchMovies :  Flow<PagingData<Movie>>? = null


    @OptIn(ExperimentalPagingApi::class)
    val popularMovies =  Pager(
        PagingConfig(
            pageSize = 20,
            enablePlaceholders = false),
        pagingSourceFactory = {
            moviesDatabase.getMoviesDao().pagingSource()
        },
        remoteMediator = PopularMoviesRemoteMediator(moviesRepo,moviesDatabase)).flow

    @OptIn(ExperimentalPagingApi::class)
    val nowPlayingMovies = Pager(
        PagingConfig(
            pageSize = 20,
            enablePlaceholders = false),
        pagingSourceFactory = {
            moviesDatabase.getMoviesDao().pagingSource()
        },
        remoteMediator = NowPlayingMoviesRemoteMediator(moviesRepo,moviesDatabase)).flow

    @OptIn(ExperimentalPagingApi::class)
    val topRatedMovies =  Pager(
        PagingConfig(pageSize = 20,
            enablePlaceholders = false),
        pagingSourceFactory = {
            moviesDatabase.getMoviesDao().pagingSource()
        },
        remoteMediator = TopRatedMoviesRemoteMediator(moviesRepo,moviesDatabase)).flow

    @OptIn(ExperimentalPagingApi::class)
    val upcomingMovies =  Pager(
        PagingConfig(pageSize = 20,
            enablePlaceholders = false),
        pagingSourceFactory = {
            moviesDatabase.getMoviesDao().pagingSource()
        },
        remoteMediator = UpComingMoviesRemoteMediator(moviesRepo,moviesDatabase)).flow



fun searchMovie(keyword: String){
     searchMovies = Pager(PagingConfig(1)){
        SearchMoviesPagingSource(keyword,moviesRepo)
    }.flow
}



    // get Movie Details
    fun getMovieDetails(movie_id:Int) = viewModelScope.launch {
        if (movie_id > 0 && movie_id != null){
            detailsResponse.postValue(Resources.Loading())
            detailsResponse.postValue( moviesRepo.getDetails(movie_id))
        }else{
            detailsResponse.postValue(Resources.Error("Error With The Movie Id ",null))
        }

    }

    fun internetConnection(context: Context) : Boolean {
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply{

            val internetStatus = getNetworkCapabilities(activeNetwork)?.run{
                when{
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ->     true
                    else -> false
                }
            }
            return internetStatus?:false

        }

    }








}