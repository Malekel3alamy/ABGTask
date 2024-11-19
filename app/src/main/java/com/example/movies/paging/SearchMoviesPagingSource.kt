package com.example.movies.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movies.api.models.Movie
import com.example.movies.repo.MoviesRepoInrerface
import retrofit2.HttpException

class SearchMoviesPagingSource(private val keyWord : String , private val moviesRepo: MoviesRepoInrerface) : PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
return null    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {

        return try{
            val currentPage = params.key ?: 1
            val response = moviesRepo.search(keyWord,currentPage)
            val data = response.data?.results

            data?.let {
                val responseData = mutableListOf<Movie>()
                responseData.addAll(data)
                LoadResult.Page(
                    data = responseData,
                    prevKey = if(currentPage == 1) null else -1,
                    nextKey = currentPage.plus(1)
                )
            }!!
        }catch (e:Exception){
            LoadResult.Error(e)
        }
        catch (httpE : HttpException){
            LoadResult.Error(httpE)
        }
    }
}