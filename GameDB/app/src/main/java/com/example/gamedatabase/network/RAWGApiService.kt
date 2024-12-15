package com.example.gamedatabase.network

import com.example.gamedatabase.model.GameDetails
import com.example.gamedatabase.model.GamesInList
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RAWGApiService {
    //https://api.rawg.io/api/games?key=3e0805133d704bd0b792f417960f423c&page=2
    @GET("games")
    suspend fun getGames(@Query("key") query: String, @Query("page") page: Int): RAWGGamesResponse

    //https://api.rawg.io/api/games/3328?key=3e0805133d704bd0b792f417960f423c
    @GET("games/{id}")
    suspend fun getGameDetails(@Path("id") gameId: Int, @Query("key") apiKey: String): GameDetails
}

@Serializable
data class RAWGGamesResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<GamesInList>
)

