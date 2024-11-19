package com.example.movies.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.movies.api.models.Movie
import com.example.movies.repo.MoviesRepoInrerface
import com.example.movies.room.MoviesDatabase
import com.example.movies.room.remotekeys.RemoteKeys
import okio.IOException
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.util.concurrent.TimeUnit


@OptIn(ExperimentalPagingApi::class)
class NowPlayingMoviesRemoteMediator(private val moviesRepo : MoviesRepoInrerface, private val moviesDatabase: MoviesDatabase) :
    RemoteMediator<Int, Movie>() {

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(4, TimeUnit.HOURS)

        return if (System.currentTimeMillis() - (moviesDatabase.getRemoteKeysDao().getCreationTime() ?: 0) < cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Movie>): MediatorResult {
        return try {
            val loadKey = when(loadType){

                LoadType.PREPEND -> {
//                    val remoteKeys = getRemoteKeyForFirstItem(state)
//                    val prevKey = remoteKeys?.prevKey
//                    prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey
                    nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)

                }

                LoadType.REFRESH ->{
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: 1
                }
            }

          val nowPlayingMoviesResponse=   moviesRepo.getNowPlayingMovies(pageNumber = loadKey).data
nowPlayingMoviesResponse?.let {
    val nowPlayingMovies = nowPlayingMoviesResponse?.results
    val endOfPaginationReached = it.results.isEmpty()
    moviesDatabase.withTransaction {
        if (loadType == LoadType.REFRESH) {
            moviesDatabase.getRemoteKeysDao().clearRemoteKeys()
            moviesDatabase.getMoviesDao().deleteAllMovies()
        }
        val prevKey = if (loadKey > 1) loadKey - 1 else null
        val nextKey = if (endOfPaginationReached) null else loadKey + 1

        val remoteKeys = nowPlayingMovies?.map {
            RemoteKeys(
                movieID = it.id!!,
                prevKey = prevKey,
                currentPage = loadKey,
                nextKey = nextKey
            )
        }
        remoteKeys?.let {
            Log.d("Cachingtimes",it.toString())
            moviesDatabase.getRemoteKeysDao().insertAll(it)
        }

        nowPlayingMovies?.let {
            moviesDatabase.getMoviesDao().upsertAllMovies(nowPlayingMovies)
        }
    }
    return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
}?:MediatorResult.Error(Exception())

        }catch (e:IOException){
            Log.e("IOException","Failed To Get Data")
            MediatorResult.Error(e)

        }catch (httpError:HttpException){
            Log.e("HttpException","Failed To Get Data")
            MediatorResult.Error(httpError)
        }


    }


    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Movie>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                moviesDatabase.getRemoteKeysDao().getRemoteKeyByMovieID(id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Movie>): RemoteKeys? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { movie ->
            moviesDatabase.getRemoteKeysDao().getRemoteKeyByMovieID(movie.id!!)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Movie>): RemoteKeys? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { movie ->
            moviesDatabase.getRemoteKeysDao().getRemoteKeyByMovieID(movie.id!!)
        }
    }
}


