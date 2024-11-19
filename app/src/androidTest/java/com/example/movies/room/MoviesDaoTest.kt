package com.example.movies.room

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import com.example.movies.api.models.Movie
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@SmallTest
class MoviesDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var database: MoviesDatabase
    private lateinit var dao: MoviesDao
    @Before
    fun setup(){

        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
        MoviesDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.getMoviesDao()
    }

    @After
    fun tearDown(){
        database.close()
    }

    @Test
    fun upsertMovieTest() = runBlocking {
        val movie = Movie(null,null,null,1,null,
            null,null,null,null,null,null,null,null,null)

        dao.upsertAllMovies(movie)

        val result = dao.getMovie(1)
        assertThat(result).isEqualTo(movie)

    }

    @Test
    fun getMovieByIdTest() = runBlocking{
        val movie = Movie(null,null,null,1,null,
            null,null,null,null,null,null,null,null,null)
        dao.upsertAllMovies(movie)

        val resultMovie = dao.getMovie(1)
        Log.d("MovieResult",resultMovie.toString())
        val resultMovies = dao.getAllMovies()

            assertThat(resultMovies.getOrAwaitValue()[0]).isEqualTo(resultMovie)
    }

    @Test
    fun deleteMovieTest() = runBlocking {
        val movie = Movie(null,null,null,1,null,
            null,null,null,null,null,null,null,null,null)
        dao.upsertAllMovies(movie)
      dao.deleteMovie(movie)
        val resultMovie = dao.getMovie(1)
        assertThat(resultMovie).isNull()
    }

    @Test
    fun deleteAllMoviesTest() = runBlocking {
        val movie = Movie(null,null,null,1,null,
            null,null,null,null,null,null,null,null,null)
        dao.upsertAllMovies(movie)
        dao.deleteAllMovies()
        val resultMovies = dao.getAllMovies()
        assertThat(resultMovies.getOrAwaitValue()).isEmpty()
    }

    @Test
    fun getAllMoviesTest() = runBlocking {
        val movie = Movie(null,null,null,1,null,
            null,null,null,null,null,null,null,null,null)
        dao.upsertAllMovies(movie)

        val resultMovie = dao.getMovie(1)
        val resultMovies = dao.getAllMovies()
        assertThat(resultMovie).isEqualTo(resultMovies.getOrAwaitValue()[0])

    }

}