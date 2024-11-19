package com.example.movies.repo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movies.api.models.Dates
import com.example.movies.api.models.Movie
import com.example.movies.api.models.MovieResponse
import com.example.movies.api.models.details.DetailsResponse
import com.example.movies.utils.Resources
import org.junit.Rule


class FakeMoviesRepo:MoviesRepoInrerface {

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val moviesList = mutableListOf<Movie>()



    private var  shouldReturnNetworkError = false

    fun shouldReturnNetworkError(value:Boolean){
        shouldReturnNetworkError = value
    }

    private fun refreshLiveData(){
        allMoviesInRoom.postValue(moviesList)
    }




   val  movie = Movie(null,null,null,1,null,null,
    null,null,null,null,null,null,
    null,null)

    val movieResponse = MovieResponse(Dates("",""),1, mutableListOf<Movie>(movie),1,1)
    val allMoviesInRoom = MutableLiveData<List<Movie>>()

    override suspend fun getTopRatedMovies(pageNumber: Int): Resources<MovieResponse> {
        return if (shouldReturnNetworkError){
            Resources.Error(" Error",null)
        }else{
            Resources.Success(movieResponse)
        }
    }

    override suspend fun getPopularMovies(pageNumber: Int): Resources<MovieResponse> {
        return if (shouldReturnNetworkError){
            Resources.Error(" Error",null)
        }else{
            Resources.Success(movieResponse)
        }
    }

    override suspend fun getNowPlayingMovies(pageNumber: Int): Resources<MovieResponse> {
        return if (shouldReturnNetworkError){
            Resources.Error(" Error",null)
        }else{
            Resources.Success(movieResponse)
        }
    }

    override suspend fun getUpcomingMovies(pageNumber: Int): Resources<MovieResponse> {
        return if (shouldReturnNetworkError){
            Resources.Error(" Error",null)
        }else{
            Resources.Success(movieResponse)
        }
    }

    override suspend fun search(keyWords: String, pageNumber: Int): Resources<MovieResponse> {
        return if (shouldReturnNetworkError){
            Resources.Error(" Error",null)
        }else{
            Resources.Success(movieResponse)
        }
    }


    override suspend fun getDetails(movie_id: Int): Resources<DetailsResponse> {
       return Resources.Success(
           DetailsResponse(true,"","",1, emptyList(),"",1,
           "", emptyList(),"","","",20.2,"", emptyList(), emptyList(),"",1,
           1, emptyList(),"","","",false,20.2,1 )
       )

    }

    override suspend fun upsert(movies: Movie) {
            moviesList.add(movies)
        refreshLiveData()
    }

    override suspend fun deleteAll() {
          moviesList.removeAll(moviesList)
        refreshLiveData()

    }

    override suspend fun getAllData(): LiveData<List<Movie>> {
        return allMoviesInRoom

    }

    override suspend fun deleteMovie(movies: Movie) {
        moviesList.remove(movies)
        refreshLiveData()
    }

    override suspend fun getMovie(id: Int): Movie {
        return movie
    }


}