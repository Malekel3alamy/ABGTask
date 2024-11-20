package com.example.movies.room

import com.example.movies.api.models.Movie


fun Movie.toMovieEntity(category:String):MovieEntity{
    return MovieEntity(
        adult = adult?:false,
        release_date = release_date?:"",
        id = id?:1,
        vote_average = vote_average?:0.0,
        genre_ids = genre_ids?: emptyList(),
        title = title?:"",
        video = video?:false,
        original_language = original_language?:"",
        original_title = original_title?:"",
        backdrop_path = backdrop_path?:"",
        popularity = popularity?:0.0,
        vote_count = vote_count?:0,
        poster_path = poster_path?:"",
        overview = overview?:"",
        category = category

    )
}