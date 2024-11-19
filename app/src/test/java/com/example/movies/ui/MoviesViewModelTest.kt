package com.example.movies.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.movies.api.models.Movie
import com.example.movies.repo.MoviesRepo
import com.example.movies.utility.MainDispatcherRule
import com.example.movies.utils.Resources
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class MoviesViewModelTest {

    @get:Rule
    private val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    val dispatcher = StandardTestDispatcher()

    lateinit var moviesViewModel : MoviesViewModel

    val pageLoadSize = 20

    @Before
     fun setup(){
         Dispatchers.setMain(dispatcher)
         val moviesRepoMock = mockk<MoviesRepo>{
             coEvery {  getDetails(any()) } returns mockk()
         }
         moviesViewModel  =  MoviesViewModel(moviesRepoMock)
        val movie = mockk<Movie>()
        val moviesList = listOf(movie,movie)



     }

    @Test
    fun  `getNowPlayingMoviesPagingSourceTestWithResponseErrorReturnError`()= runTest {
    /*    val moviesRepoMock = mockk<MoviesRepo>()
        val resultMovies = mutableListOf<Movie>()
        //  val movieResponse = MovieResponse(Dates("1","1"),1,resultMovies,1,1)
        // val  movieResponse = mockk<MovieResponse>()
        val response  = getMockOkResponse(1,resultMovies)

        coEvery { moviesRepoMock.getNowPlayingMovies(1) } returns response

        val nowPlayingPagingSource = NowPlayingMoviesPagingSource(moviesRepoMock)

        val refreshLoadParam = PagingSource.LoadParams.Refresh<Int>(
            null,pageLoadSize,
            false)

        val actualLoadResult = nowPlayingPagingSource.load(refreshLoadParam)
        val expectedLoadResult = PagingSource.LoadResult.Page(
            data = moviesList,

        )
*/

    }


    @Test
    fun `upsertMovieToRoomAndReturnTrue`()= runTest {
        val movie = Movie(null,null,null,1,null,
            null,null,null,null,null,null,null,null,null)
        val upsertResult = moviesViewModel.upsertMovies(movie)

        val result = moviesViewModel.getMovie(1)
        assertThat(result).isEqualTo(upsertResult)

    }

    @Test
     fun `getMovieDetails with a wrong id returns Error`(){

    moviesViewModel.getMovieDetails(0)
    dispatcher.scheduler.advanceUntilIdle()
    assert(moviesViewModel.detailsResponse.value is Resources.Error)

    }
}















  /*  private fun getMockOkResponse(total : Int, list: MutableList<Movie>) : Resources<MovieResponse> {
        val mockMoviesResponse = MovieResponse(
            Dates("1","1"),
            1,list,1,
            1)
        return Resources.Success(mockMoviesResponse)
    }
    }*/


