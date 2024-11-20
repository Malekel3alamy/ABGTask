package com.example.movies.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.movies.repo.MoviesRepoInrerface
import com.example.movies.room.MovieEntity
import com.example.movies.room.MoviesDatabase
import com.example.movies.room.remotekeys.RemoteKeys
import com.example.movies.room.toMovieEntity
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class TopRatedMoviesRemoteMediator(private val moviesRepo : MoviesRepoInrerface, private val moviesDatabase: MoviesDatabase) : RemoteMediator<Int, MovieEntity>() {

    private val CATEGORY_NAME = "TopRated"

    override suspend fun initialize(): InitializeAction {
        if (moviesDatabase.getRemoteKeysDao().getCreationTime(CATEGORY_NAME) != null){
            val cacheTimeout = TimeUnit.MILLISECONDS.convert(4, TimeUnit.HOURS)

            return if (System.currentTimeMillis() - (moviesDatabase.getRemoteKeysDao().getCreationTime(CATEGORY_NAME)!!) == cacheTimeout) {
                InitializeAction.LAUNCH_INITIAL_REFRESH
            } else {

                InitializeAction.SKIP_INITIAL_REFRESH

            }
        }else{
            return InitializeAction.LAUNCH_INITIAL_REFRESH
        }

    }
    override suspend fun load(loadType: LoadType, state: PagingState<Int, MovieEntity>): MediatorResult {
        return try {
            val currentPage = when(loadType){

                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)

                }
                LoadType.APPEND -> {
                    Log.d("POPULAR_REMOTE_MEDIATOR","append TopRated ")
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey

                    if (nextKey != null){
                        moviesDatabase.withTransaction {
                            moviesRepo.getTopRatedMovies(nextKey)
                        }
                    }
                    nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)

                }

                LoadType.REFRESH ->{
                    Log.d("POPULAR_REMOTE_MEDIATOR","refresh topRated ")
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: 1
                }
            }

            val topRatedMoviesResponse=   moviesRepo.getTopRatedMovies(pageNumber = currentPage).data
            topRatedMoviesResponse?.let {

                val topRatedMovies = it.results
                val endOfPaginationReached = it.results.isEmpty()

                moviesDatabase.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        moviesDatabase.getRemoteKeysDao().clearRemoteKeysWithCategory(CATEGORY_NAME)
                        moviesDatabase.getMoviesDao().deleteMoviesWithCategory(CATEGORY_NAME)
                    }

                    val prevKey = if (currentPage > 1) currentPage.minus(1)  else -1
                    val nextKey = if (endOfPaginationReached) null else currentPage.plus(1)

                    val remoteKeys = topRatedMovies.map {
                        RemoteKeys(
                            id = it.id!!,
                            prevKey = prevKey,
                            nextKey = nextKey,
                            category = CATEGORY_NAME
                        )
                    }
                    Log.d("REMOTEKEYSTOPRATED",remoteKeys.toString())
                    moviesDatabase.getRemoteKeysDao().insertAll(remoteKeys)

                    val entities = topRatedMovies.map { it.toMovieEntity(CATEGORY_NAME) }
                    moviesDatabase.getMoviesDao().upsertAllMovies(entities)
                }

                return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            }?: MediatorResult.Error(Exception())

        }catch (e: IOException){
            Log.e("IOException","Failed To Get Data")
            MediatorResult.Error(e)

        }catch (httpError:HttpException){
            Log.e("HttpException","Failed To Get Data")
            MediatorResult.Error(httpError)
        }


    }


    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, MovieEntity>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                moviesDatabase.getRemoteKeysDao().getRemoteKeyByMovieID(id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MovieEntity>): RemoteKeys? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { movie ->
            moviesDatabase.getRemoteKeysDao().getRemoteKeyByMovieID(movie.id!!)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MovieEntity>): RemoteKeys? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { movie ->
            moviesDatabase.getRemoteKeysDao().getRemoteKeyByMovieID(movie.id!!)
        }
    }


}