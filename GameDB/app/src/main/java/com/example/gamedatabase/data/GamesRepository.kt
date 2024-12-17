package com.example.gamedatabase.data

import com.example.gamedatabase.model.GameDetails
import com.example.gamedatabase.model.GamesInList
import com.example.gamedatabase.network.RAWGApiService

interface GamesRepository {
    suspend fun getGames(query:String, page: Int): List<GamesInList>
    suspend fun getGameDetails(id: Int, query: String): GameDetails
    suspend fun getGameSearch(key: String, query: String): List<GamesInList>
    suspend fun getGamePlatform(key: String, filter: String): List<GamesInList>
    suspend fun getGameGenre(key: String, genre: String): List<GamesInList>
    suspend fun getGameSearchPlatform(key: String, query: String, filter: String): List<GamesInList>
    suspend fun getGameSearchGenre(key: String, query: String, genre: String): List<GamesInList>
    suspend fun getGameSearchPlatformGenre(key: String, query: String, filter: String, genre: String): List<GamesInList>
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

    override suspend fun getGamePlatform(key: String, filter: String): List<GamesInList> {
        return rawgApiService.getGamePlatform(key, filter).results
    }

    override suspend fun getGameGenre(key: String, genre: String): List<GamesInList> {
        return rawgApiService.getGameGenre(key, genre).results
    }

    override suspend fun getGameSearchPlatform(key: String, query: String, filter: String): List<GamesInList> {
        return rawgApiService.getGameSearchPlatform(key, query, filter).results
    }

    override suspend fun getGameSearchGenre(key: String, query: String, genre: String): List<GamesInList> {
        return rawgApiService.getGameSearchGenre(key, query, genre).results
    }

    override suspend fun getGameSearchPlatformGenre(key: String, query: String, filter: String, genre: String
    ): List<GamesInList> {
        return rawgApiService.getGameSearchFilterGenre(key, query, filter, genre).results
    }
}

