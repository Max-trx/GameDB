package com.example.gamedatabase.model

import kotlinx.serialization.Serializable

@Serializable
data class GamesInList(
    val id: Int,
    val name: String,
    val description: String? = null,
    val platforms: List<Platform>? = null,
    val metacritic: Int? = null,
    val background_image: String? = null,
    val playtime: Int? = null,
    val rating: Double? = null,
    val ratings_count: Int? = null,
    val genres: List<Genre>? = null,
    val released: String? = null
)

@Serializable
data class GameDetails(
    val id: Int,
    val name: String,
    val description: String? = null,
    val platforms: List<Platform>? = null,
    val metacritic: Int? = null,
    val background_image: String? = null,
    val playtime: Int? = null,
    val rating: Double? = null,
    val ratings_count: Int? = null,
    val genres: List<Genre>? = null,
    val released: String? = null
)

@Serializable
data class Genre (
    val id: Long,
    val name: String,
    val games_count: Long? = null,
    val image_background: String? = null
)

@Serializable
data class Platform(
    val platform: PlatformSpec,
    val released_at: String? = null,
    val requirementsEn: RequirementsEn? = null,

    )

@Serializable
data class PlatformSpec(
    val name: String
)

@Serializable
data class RequirementsEn (
    val minimum: String? = null,
    val recommended: String? = null
)