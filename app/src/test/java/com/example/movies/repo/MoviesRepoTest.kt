package com.example.movies.repo

import com.example.movies.utils.Resources
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class MoviesRepoTest {


    lateinit var moviesRepo: MoviesRepo

    @Before
    fun setup(){
        moviesRepo = mockk()

    }

    @Test
    fun `testGetMovieDetails With Response Error Return ResourcesError`(){

        runTest {
            val result = moviesRepo.getDetails(1)
            assertThat(result).isEqualTo(Resources.Error("",null))
        }
    }

}