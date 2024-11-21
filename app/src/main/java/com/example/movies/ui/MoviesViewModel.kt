package com.example.movies.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import com.example.movies.api.models.Movie
import com.example.movies.api.models.details.DetailsResponse
import com.example.movies.api.models.images.ImagesResponse
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
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor( private val moviesRepo:  MoviesRepoInrerface,private val moviesDatabase: MoviesDatabase) : ViewModel() {



    var detailsResponse =  MutableLiveData<Resources<DetailsResponse>>()

    var imageResponse = MutableLiveData<Resources<ImagesResponse>>()


    var searchMovies :  Flow<PagingData<Movie>>? = null



    @OptIn(ExperimentalPagingApi::class)
    val nowPlayingMovies = Pager(
        PagingConfig(
            pageSize = 70,
            enablePlaceholders = false,
            prefetchDistance = 10,
            initialLoadSize = 40),
        pagingSourceFactory = {

            moviesDatabase.getMoviesDao().getMoviesByCategories("NowPlaying")
        },
        remoteMediator = NowPlayingMoviesRemoteMediator(moviesRepo,moviesDatabase)
    ).flow



    @OptIn(ExperimentalPagingApi::class)
    val popularMovies =  Pager(
        PagingConfig(
            pageSize = 50,
            enablePlaceholders = false,
            prefetchDistance = 70),
        remoteMediator = PopularMoviesRemoteMediator(moviesRepo,moviesDatabase),
        pagingSourceFactory = {
            moviesDatabase.getMoviesDao().getMoviesByCategories("Popular")
        },
        ).flow

    @OptIn(ExperimentalPagingApi::class)
    val topRatedMovies =  Pager(
        PagingConfig(pageSize = 50,
            enablePlaceholders = false,
            prefetchDistance = 10),
        pagingSourceFactory = {
            moviesDatabase.getMoviesDao().getMoviesByCategories("TopRated")
        },
        remoteMediator = TopRatedMoviesRemoteMediator(moviesRepo,moviesDatabase)).flow

    @OptIn(ExperimentalPagingApi::class)
    val upcomingMovies =  Pager(
        PagingConfig(pageSize = 70,
            enablePlaceholders = false),
        pagingSourceFactory = {

            moviesDatabase.getMoviesDao().getMoviesByCategories("UpComing")
        },
        remoteMediator = UpComingMoviesRemoteMediator(moviesRepo,moviesDatabase)).flow.cachedIn(viewModelScope)



fun searchMovie(keyword: String){
     searchMovies = Pager(PagingConfig(1)){
        SearchMoviesPagingSource(keyword,moviesRepo)
    }.flow
}



    // get Movie Details
    fun getMovieDetails(movie_id:Int) = viewModelScope.launch {

        if (movie_id > 0 && movie_id != null){
            detailsResponse.postValue(Resources.Loading())
            delay(3000L)
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

    // get Movie Images

     fun getMovieImages(id:Int) = viewModelScope.launch {
        if (id > 0 && id != null){
            imageResponse.postValue(Resources.Loading())
            delay(1000L)
            imageResponse.postValue(moviesRepo.getMovieImages(id))
        }else{
            imageResponse.postValue(Resources.Error("Error With The Movie Id",null))
        }
    }








}