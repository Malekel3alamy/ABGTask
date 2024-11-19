package com.example.movies.room

import com.example.movies.api.models.Movie


fun Movie.toMovieEntity():MovieEntity{
    return MovieEntity(
        adult = adult,
        release_date = release_date,
        id = id,
        vote_average = vote_average,
        genre_ids = genre_ids,
        title = title,
        video = video,
        original_language = original_language,
        original_title = original_title,
        backdrop_path = backdrop_path,
        popularity = popularity,
        vote_count = vote_count,
        poster_path = poster_path,
        overview = overview,

    )
}