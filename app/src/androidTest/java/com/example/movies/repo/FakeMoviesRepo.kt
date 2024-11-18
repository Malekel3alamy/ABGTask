package com.example.movies.repo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movies.api.MoviesApi
import com.example.movies.models.Dates
import com.example.movies.models.Movie
import com.example.movies.models.MovieResponse
import com.example.movies.models.details.DetailsResponse
import com.example.movies.paging.NowPlayingMoviesPagingSource
import com.example.movies.paging.TopRatedMoviesPagingSource
import com.example.movies.paging.UpComingMoviesPagingSource
import com.example.movies.room.MoviesDatabase
import com.example.movies.utils.Resources
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.Response


class FakeMoviesRepo:MoviesRepoInrerface {

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

      private val moviesList = mutableListOf<Movie>()
    private  lateinit var movie: Movie
    private   lateinit var allMoviesInRoom : MutableLiveData<List<Movie>>

    private var  shouldReturnNetworkError = false

    fun shouldReturnNetworkError(value:Boolean){
        shouldReturnNetworkError = value
    }

    private fun refreshLiveData(){
        allMoviesInRoom.postValue(moviesList)
    }

    private lateinit var movieResponse:MovieResponse


    @Before
    fun setup(){
      movie = Movie(null,null,null,1,null,null,
          null,null,null,null,null,null,
          null,null)

        movieResponse = MovieResponse(Dates("",""),1, mutableListOf<Movie>(movie),1,1)
        allMoviesInRoom = MutableLiveData<List<Movie>>()

    }

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
       return  Resources.Success( DetailsResponse(true,"","",1, emptyList(),"",1,
           "", emptyList(),"","","",20.2,"", emptyList(), emptyList(),"",1,
           1, emptyList(),"","","",false,20.2,1 ))

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