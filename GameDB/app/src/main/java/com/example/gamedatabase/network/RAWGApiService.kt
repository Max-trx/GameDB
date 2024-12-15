package com.example.gamedatabase.network

import com.example.gamedatabase.model.Games
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

interface RAWGApiService {
    @GET("games")
    suspend fun getGames(@Query("key") query: String): RAWGGamesResponse

}

@Serializable
data class RAWGGamesResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Games>
)

