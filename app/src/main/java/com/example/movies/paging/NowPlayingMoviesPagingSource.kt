package com.example.movies.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.movies.models.Movie
import com.example.movies.repo.MoviesRepo
import com.example.movies.repo.MoviesRepoInrerface
import com.example.movies.room.MoviesDatabase
import com.example.movies.utils.Resources
import kotlinx.coroutines.delay
import okio.IOException
import retrofit2.HttpException


@OptIn(ExperimentalPagingApi::class)
class NowPlayingMoviesPagingSource(private val moviesRepo : MoviesRepoInrerface,private val moviesDatabase: MoviesDatabase) :
    RemoteMediator<Int, Movie>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Movie>): MediatorResult {
        return try {
            val loadKey = when(loadType){

                LoadType.PREPEND -> {
                    return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                }
                LoadType.APPEND -> {
                   val lastItem = state.lastItemOrNull()
                    if (lastItem == null){
                        1
                    }else{
                        (lastItem.id!!.div(state.config.pageSize)).plus(1)
                    }
                }

                LoadType.REFRESH ->1
            }
          val nowPlayingMovies=   moviesRepo.getNowPlayingMovies(pageNumber = loadKey)

            moviesDatabase.withTransaction {
                if (loadType == LoadType.REFRESH){
                    moviesDatabase.getMoviesDao().deleteAllMovies()
                }
                nowPlayingMovies.data?.let {
                    moviesDatabase.getMoviesDao().upsertAllMovies(it.results)
                }


            }

            MediatorResult.Success(endOfPaginationReached = nowPlayingMovies.data?.page == null)
        }catch (e:IOException){
            MediatorResult.Error(e)
        }catch (httpError:HttpException){
            MediatorResult.Error(httpError)
        }
    }


}