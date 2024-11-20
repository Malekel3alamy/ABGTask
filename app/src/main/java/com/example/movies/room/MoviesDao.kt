package com.example.movies.room

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun upsertAllMovies(result :List<MovieEntity>)

    @Query("SELECT * FROM  movies")
     fun getAllMovies() : LiveData<List<MovieEntity>>

    @Query("SELECT * FROM  movies")
    fun getAllMoviesEntities() : PagingSource<Int,MovieEntity>



    @Query("DELETE FROM movies WHERE category =:category")
    suspend fun deleteMoviesWithCategory(category: String)

    @Query("DELETE  FROM movies")
    suspend fun deleteAllMovies()


    @Query("SELECT * FROM  movies WHERE category = :category")
     fun getMoviesByCategories(category:String):PagingSource<Int,MovieEntity>








}