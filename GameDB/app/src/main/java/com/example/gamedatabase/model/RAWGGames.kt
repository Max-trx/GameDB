package com.example.gamedatabase.model

import kotlinx.serialization.Serializable

@Serializable
data class Games(
    val id: Int,
    val name: String,
    val description: String? = null,
    val platforms: List<PlatformElement>,
    val metacritic: Int,
    val background_image: String,
    val playtime: Int,
    val rating: Double,
    val ratings_count: Int,
    val genres: List<Genre>,
    val released: String
)

@Serializable
data class Genre (
    val id: Long,
    val name: String,
    val games_count: Long,
    val image_background: String
)

@Serializable
data class PlatformElement (
    val released_at: String,
    val requirementsEn: RequirementsEn? = null,

)

@Serializable
data class RequirementsEn (
    val minimum: String,
    val recommended: String
)