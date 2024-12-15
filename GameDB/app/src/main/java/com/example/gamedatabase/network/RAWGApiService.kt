package com.example.gamedatabase.network

import com.example.gamedatabase.model.GameDetails
import com.example.gamedatabase.model.GamesInList
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

interface RAWGApiService {
    //@GET("games")
    //suspend fun getGames(@Query("key") query: String): RAWGGamesResponse
    @GET("games")
    suspend fun getGames(@Query("key") query: String, @Query("page") page: Int): RAWGGamesResponse

    @GET("games")
    suspend fun getGameDetails(@Query("key") query: String, @Query("id") id: Int): GameDetails
}

@Serializable
data class RAWGGamesResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<GamesInList>
)

