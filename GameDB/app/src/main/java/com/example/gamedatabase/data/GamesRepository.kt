package com.example.gamedatabase.data

import com.example.gamedatabase.model.GamesInList
import com.example.gamedatabase.network.RAWGApiService
import com.example.gamedatabase.network.RAWGGamesResponse

interface GamesRepository {
    suspend fun getGames(query:String, page: Int): List<GamesInList>
}

class NetworkGamesRepository(private val rawgApiService: RAWGApiService) : GamesRepository {
    override suspend fun getGames(query: String, page: Int): List<GamesInList> {
        return rawgApiService.getGames(query, page).results
    }
}

