package com.example.movies.api

import com.example.movies.api.models.MovieResponse
import com.example.movies.api.models.details.DetailsResponse
import com.example.movies.api.models.images.ImagesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {

    @GET("search/movie")
    suspend fun searchForMovies(

        @Query("query")
        query: String ,
        @Query("page")
        pageNumber: Int  ,
    ) : Response<MovieResponse>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page")
        page : Int  ,


    ): Response<MovieResponse>
    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(

        @Query("page")
        page : Int  ,

    ) : Response<MovieResponse>
    @GET("movie/popular")
    suspend fun getPopularMovies(

        @Query("page")
        page : Int  ,


    ) : Response<MovieResponse>
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page")
        page : Int

    ): Response<MovieResponse>

    @GET("movie/{movie_id}")
    suspend fun getDetails(
        @Path("movie_id")
        movieId:Int
    ):Response<DetailsResponse>

    @GET("movie/{movie_id}/images")
    suspend fun getMovieImages (
        @Path("movie_id")
        movieId:Int
    ):Response<ImagesResponse>

}