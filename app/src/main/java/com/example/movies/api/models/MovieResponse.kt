package com.example.movies.api.models

data class MovieResponse(
    val dates: Dates,
    var page: Int,
    val results: MutableList<Movie>,
    val total_pages: Int,
    val total_results: Int
)