package com.example.movies.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movies.api.RetrofitInstance
import com.example.movies.models.Movie
import com.example.movies.models.MovieResponse
import com.example.movies.models.details.DetailsResponse
import com.example.movies.room.MoviesDatabase
import com.example.movies.utils.Resources
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


open class MoviesRepo @Inject constructor(val db: MoviesDatabase) :MoviesRepoInrerface {



    override  suspend fun getTopRatedMovies(pageNumber:Int) : Resources<MovieResponse> {
        return try {
            val response =  RetrofitInstance.api.getTopRatedMovies(pageNumber)
            if (response.isSuccessful){
                response.body()?.let {
                    return@let Resources.Success(it)
                } ?: Resources.Error(" An UnKnown Error Occured " ,null)
            }else{
                Resources.Error(" An UnKnown Error Occured " ,null)
            }
        }catch (e:Exception){
            Resources.Error("Couldn't reach the server. Check your internet connection", null)
        }
    }




    override   suspend fun getPopularMovies(pageNumber:Int): Resources<MovieResponse> {


        return try {
            val response=  RetrofitInstance.api.getPopularMovies(pageNumber)
            if (response.isSuccessful){
                response.body()?.let {
                    return@let Resources.Success(it)
                } ?: Resources.Error(" An UnKnown Error Occured " ,null)
            }else{
                Resources.Error(" An UnKnown Error Occured " ,null)
            }
        }catch (e:Exception){
            Resources.Error("Couldn't reach the server. Check your internet connection", null)
        }

    }




    override    suspend fun getNowPlayingMovies(pageNumber:Int) : Resources<MovieResponse>{

        return try {
            val response= RetrofitInstance. api.getNowPlayingMovies(pageNumber)
            if (response.isSuccessful){
                response.body()?.let {
                    return@let Resources.Success(it)
                } ?: Resources.Error(" An UnKnown Error Occured  response body  is null " ,null)
            }else{
                return Resources.Error(" api request was not successfully ", null)
            }

        }catch (e:Exception){
            Resources.Error("Couldn't reach the server. Check your internet connection", null)
        }

    }

    override  suspend fun getUpcomingMovies(pageNumber:Int): Resources<MovieResponse> {
        return try {
            val response=   RetrofitInstance.api.getUpcomingMovies(pageNumber)
            if (response.isSuccessful){
                response.body()?.let {
                    return@let Resources.Success(it)
                } ?: Resources.Error(" An UnKnown Error Occured " ,null)
            }else{
                Resources.Error(" An UnKnown Error Occured " ,null)
            }
        }catch (e:Exception){
            Resources.Error("Couldn't reach the server. Check your internet connection", null)
        }
    }


     // Search For Movies
     override     suspend fun  search(keyWords: String,pageNumber:Int): Resources<MovieResponse> {
        return try {
            val response = RetrofitInstance.api.searchForMovies(keyWords, pageNumber)
            if (response.isSuccessful){
                response.body()?.let {
                    return@let Resources.Success(it)
                } ?: Resources.Error(" An UnKnown Error Occured " ,null)
            }else{
                Resources.Error(" An UnKnown Error Occured " ,null)
            }
        }catch (e:Exception){
            Resources.Error("Couldn't reach the server. Check your internet connection", null)
        }
     }

// Get Details
override suspend fun  getDetails(movie_id:Int) : Resources<DetailsResponse> {
    val response =  RetrofitInstance.api.getDetails(movie_id)
return try{
    if (response.isSuccessful){
        response.body()?.let {
            return@let Resources.Success(it)
        } ?:Resources.Error(" An UnKnown Error Occured " ,null)
    }else{
        Resources.Error(" An UnKnown Error Occured " ,null)
    }
}catch (e:Exception){
    Resources.Error(" An UnKnown Error Occured " ,null)
}
}



   // Insert Data To Room
    override suspend fun  upsert(movies:Movie)  = db.getMoviesDao().upsert(movies)

   // Delete All Room Database
    override suspend fun deleteAll() = db.getMoviesDao().deleteAllMovies()

   // Get All Data From Room
     override suspend   fun getAllData(): LiveData<List<Movie>> = db.getMoviesDao().getAllMovies()

   // Delete One Movie

    override suspend fun deleteMovie(movies:Movie) = db.getMoviesDao().deleteMovie(movies)

    override  suspend fun getMovie(id:Int) :Movie = db.getMoviesDao().getMovie(id)

}