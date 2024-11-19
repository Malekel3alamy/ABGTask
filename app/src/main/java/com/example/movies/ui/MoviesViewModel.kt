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
import com.example.movies.paging.PopularMoviesPagingSource
import com.example.movies.paging.SearchMoviesPagingSource
import com.example.movies.paging.TopRatedMoviesPagingSource
import com.example.movies.paging.UpComingMoviesPagingSource
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

    var roomMovies : MutableLiveData<List<MovieEntity>>? = null

    var searchMovies :  Flow<PagingData<Movie>>? = null


    val popularMovies = Pager(PagingConfig(5)){
        PopularMoviesPagingSource(moviesRepo)
    }.flow.cachedIn(viewModelScope)

    @OptIn(ExperimentalPagingApi::class)
    val nowPlayingMovies = Pager(
        PagingConfig(20),
        pagingSourceFactory = {
            moviesDatabase.getMoviesDao().pagingSource()
        },
        remoteMediator = NowPlayingMoviesRemoteMediator(moviesRepo,moviesDatabase)).flow.cachedIn(viewModelScope)

    val topRatedMovies = Pager(PagingConfig(5)){
        TopRatedMoviesPagingSource(moviesRepo)
    }.flow.cachedIn(viewModelScope)

    val upcomingMovies = Pager(PagingConfig(5)){
        UpComingMoviesPagingSource(moviesRepo)
    }.flow.cachedIn(viewModelScope)



fun searchMovie(keyword: String){
     searchMovies = Pager(PagingConfig(1)){
        SearchMoviesPagingSource(keyword,moviesRepo)
    }.flow.cachedIn(viewModelScope)
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

  /*  suspend fun  upsertMovies(movies : List<MovieEntity>):Boolean {
        var  result = false
        viewModelScope.async {

             moviesRepo.upsertAllMovies(movies)
            result = true

        }.await()
        return result
    }*/
  /*  // Get All Movies
    suspend fun getAllMoviesFromRoom() {
        roomMovies = MutableLiveData()
        val movies= moviesRepo.getAllData()
        if (movies!= null){
            roomMovies?.postValue(movies.value)
        }
    }*/


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

    // delete data inside database
    fun deleteAll()=viewModelScope.launch {
        moviesRepo.deleteAll()
    }



    fun  updateMoviesDataAndApi() = viewModelScope.launch {

        deleteAll()
       // upsertMovies(roomMovies?.value!!.toList() )

    }

//    suspend fun getMovie(id:Int) :Boolean {
//        var movie :Movie?= null
//        viewModelScope.async {
//             movie = moviesRepo.getMovie(id)
//
//        }.await()
//
//        if (movie == null){
//            return false
//        }else{
//            return true
//        }
//    }




}