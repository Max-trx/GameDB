package com.example.gamedatabase.data

import com.example.gamedatabase.model.GameDetails
import com.example.gamedatabase.model.GamesInList
import com.example.gamedatabase.network.RAWGApiService

interface GamesRepository {
    suspend fun getGames(query:String, page: Int): List<GamesInList>
    suspend fun getGameDetails(id: Int, query: String): GameDetails
    suspend fun getGameSearch(key: String, query: String): List<GamesInList>
    suspend fun getGameFilter(key: String, filter: String): List<GamesInList>
    suspend fun getGameSearchFilter(key: String, query: String, filter: String): List<GamesInList>
}

class NetworkGamesRepository(private val rawgApiService: RAWGApiService) : GamesRepository {
    override suspend fun getGames(query: String, page: Int): List<GamesInList> {
        return rawgApiService.getGames(query, page).results
    }

    override suspend fun getGameDetails(gameId: Int, query: String): GameDetails {
        return  rawgApiService.getGameDetails(gameId, query)
    }

    override suspend fun getGameSearch(key: String, query: String): List<GamesInList> {
        return rawgApiService.getGameSearch(key, query).results
    }

    override suspend fun getGameFilter(key: String, filter: String): List<GamesInList> {
        return rawgApiService.getGameFilter(key, filter).results
    }

    override suspend fun getGameSearchFilter(key: String, query: String, filter: String): List<GamesInList> {
        return rawgApiService.getGameSearchFilter(key, query, filter).results
    }
}

