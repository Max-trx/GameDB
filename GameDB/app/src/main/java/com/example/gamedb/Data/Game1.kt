package com.example.gamedb.Data

import java.util.Date

data class Game1(
    val id: Int,
    val name: String,
    val description: String,
    val platforms: List<Platform>,
    val metacritic: Int,
    val background_image: String,
    val playtime: Int,
    val rating: Int,
    val ratings_count: Int,
    val released: Date,
)