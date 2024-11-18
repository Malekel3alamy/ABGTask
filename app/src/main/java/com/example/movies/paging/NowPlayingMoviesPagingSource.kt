package com.example.movies.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movies.models.Movie
import com.example.movies.repo.MoviesRepo
import com.example.movies.repo.MoviesRepoInrerface
import com.example.movies.utils.Resources
import kotlinx.coroutines.delay
import okio.IOException
import retrofit2.HttpException


class NowPlayingMoviesPagingSource(private val moviesRepo : MoviesRepoInrerface) : PagingSource<Int,Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
            return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {

        return try {
            val currentPage = params.key ?: 1
            val response = moviesRepo.getNowPlayingMovies(currentPage)
            var data = mutableListOf<Movie>()
            when(response){
                is Resources.Error -> {
                    Log.d("PagingError","${response.message}  ")
                }
                is Resources.Loading -> {
                    Log.d("PagingError"," Loading")
                }
                is Resources.Success -> {
                    Log.d("PagingError"," Succeeded ")
                     data  = response.data!!.results

                }
            }
            // delay(2000L)
            val responseData = mutableListOf<Movie>()
            responseData.addAll(data)
            LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage == 1) null else -1,
                nextKey = currentPage.plus(1)
            )
        }catch (e:IOException){
            Log.d("PagingError",e.toString())
            LoadResult.Error(e)
        }
        catch (httpE : HttpException){
            Log.d("PagingError",httpE.toString())
            LoadResult.Error(httpE)
        }
    }

}