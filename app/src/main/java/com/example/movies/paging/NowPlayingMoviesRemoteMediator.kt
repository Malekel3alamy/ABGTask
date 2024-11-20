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
import okio.IOException
import retrofit2.HttpException
import java.util.concurrent.TimeUnit


@OptIn(ExperimentalPagingApi::class)
class NowPlayingMoviesRemoteMediator(private val moviesRepo : MoviesRepoInrerface, private val moviesDatabase: MoviesDatabase) :
    RemoteMediator<Int, MovieEntity>() {


private val CATEGORY_NAME = "NowPlaying"

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(4, TimeUnit.HOURS)

        return if (System.currentTimeMillis() - (moviesDatabase.getRemoteKeysDao().getCreationTime() ?: 0) != cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, MovieEntity>): MediatorResult {
        return try {

            val currentPage = when(loadType){

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevKey?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    prevPage

                }
                LoadType.APPEND -> {
                    Log.d("POPULAR_REMOTE_MEDIATOR","append nowplaying")
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey
                     Log.d("NEXTKEy",nextKey.toString())
                    nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }

                LoadType.REFRESH ->{
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: 1
                }
            }


            val nowPlayingMoviesResponse=   moviesRepo.getNowPlayingMovies(pageNumber = currentPage).data


  nowPlayingMoviesResponse?.let {
    val nowPlayingMovies = it.results

    val endOfPaginationReached = it.results.isEmpty()

Log.d("END_OF_PAG",endOfPaginationReached.toString())

    moviesDatabase.withTransaction {
        if (loadType == LoadType.REFRESH) {
            moviesDatabase.getRemoteKeysDao().clearRemoteKeys()
            moviesDatabase.getMoviesDao().deleteMoviesWithCategory(CATEGORY_NAME)
        }
        val prevKey = if (currentPage > 1) currentPage.minus(1)  else -1
        val nextKey = if (endOfPaginationReached) null else currentPage.plus(1)


        val remoteKeys = nowPlayingMovies.map {
            RemoteKeys(
                id = it.id!!,
                prevKey = prevKey,
                nextKey = nextKey,
            )
        }


        moviesDatabase.getRemoteKeysDao().insertAll(remoteKeys)

        val nowPlayingMoviesEntities = nowPlayingMovies.map { it.toMovieEntity(CATEGORY_NAME) }

        moviesDatabase.getMoviesDao().upsertAllMovies(nowPlayingMoviesEntities)
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
            moviesDatabase.getRemoteKeysDao().getRemoteKeyByMovieID(movie.id)
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


