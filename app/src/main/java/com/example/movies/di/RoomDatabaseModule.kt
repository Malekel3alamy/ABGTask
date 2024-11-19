package com.example.movies.di

import android.content.Context
import com.example.movies.repo.MoviesRepo
import com.example.movies.repo.MoviesRepoInrerface
import com.example.movies.room.MoviesDatabase
import com.example.movies.room.remotekeys.RemoteKeysDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDatabaseModule {

    @Provides
    @Singleton
    fun provideRoomModule(@ApplicationContext context: Context) : MoviesDatabase {

        return MoviesDatabase.getInstance(context)

    }




    @Provides
    @Singleton
    fun provideMoviesRepoInterface (db:MoviesDatabase) : MoviesRepoInrerface{
        return MoviesRepo(db)
    }

    @Singleton
    @Provides
    fun provideRemoteKeysDao(moviesDatabase: MoviesDatabase): RemoteKeysDao = moviesDatabase.getRemoteKeysDao()
}