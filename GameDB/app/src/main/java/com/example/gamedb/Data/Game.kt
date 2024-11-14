package com.example.gamedb.Data

import java.util.Date

data class Game(
    val id: Int,
    val name: String,
    val description: String,
    val platforms: List<PlatformGame>,
    val metacritic: Int,
    val background_image: String,
    val playtime: Int,
    val rating: Int,
    val ratings_count: Int,
    val reactions: Reactions,
    val released: Date,
)