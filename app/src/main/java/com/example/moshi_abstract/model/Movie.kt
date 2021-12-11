package com.example.moshi_abstract.model

import com.squareup.moshi.Json

data class Movie (
    @Json(name = "vote_count") val voteCount: Int = -1,
    val id: Int,
    val title: String,
    @Json(name = "image_path") val imagePath: String,
    val genre_ids: List<Int>,
    val overview: String
)
