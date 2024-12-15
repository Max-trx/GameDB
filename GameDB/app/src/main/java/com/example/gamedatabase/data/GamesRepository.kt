package com.example.gamedatabase.data

import com.example.gamedatabase.model.GameDetails
import com.example.gamedatabase.model.GamesInList
import com.example.gamedatabase.network.RAWGApiService

interface GamesRepository {
    suspend fun getGames(query:String, page: Int): List<GamesInList>
    suspend fun getGameDetails(id: Int, query: String): GameDetails
}

class NetworkGamesRepository(private val rawgApiService: RAWGApiService) : GamesRepository {
    override suspend fun getGames(query: String, page: Int): List<GamesInList> {
        return rawgApiService.getGames(query, page).results
    }

    override suspend fun getGameDetails(gameId: Int, query: String): GameDetails {
        return  rawgApiService.getGameDetails(gameId, query)
    }
}

