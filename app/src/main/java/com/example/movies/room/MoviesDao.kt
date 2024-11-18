package com.example.movies.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movies.models.Movie
import com.example.movies.models.MovieResponse


@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(result :Movie)

    @Query(" SELECT * FROM  movies")
     fun getAllMovies() : LiveData<List<Movie>>
    @Query("SELECT * FROM movies WHERE id = :id")
    suspend fun getMovie(id:Int) : Movie

    @Query("DELETE FROM movies")
    suspend fun deleteAllMovies()

    @Delete
    suspend fun deleteMovie(movie: Movie)




}