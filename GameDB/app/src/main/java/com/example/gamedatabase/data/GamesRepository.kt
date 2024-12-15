package com.example.gamedatabase.data

import com.example.gamedatabase.model.Games
import com.example.gamedatabase.network.RAWGApiService
import com.example.gamedatabase.network.RAWGGamesResponse

interface GamesRepository {
    suspend fun getGames(query: String): List<Games>
}

class NetworkGamesRepository(private val rawgApiService: RAWGApiService) : GamesRepository {
    override suspend fun getGames(query: String): List<Games> {
        return rawgApiService.getGames(query).results
    }
}

