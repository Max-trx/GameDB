package com.example.gamedatabase.data

import com.example.gamedatabase.network.RAWGApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
interface AppContainer {
    val gamesRepository: GamesRepository
}

class DefaultAppContainer : AppContainer {

    val json = Json {
        ignoreUnknownKeys = true
        // Ajoutez d'autres configurations si nécessaire
    }
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.rawg.io/api/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val rawgGamesApiService: RAWGApiService by lazy {
        retrofit.create(RAWGApiService::class.java)
    }

    override val gamesRepository: GamesRepository by lazy {
        NetworkGamesRepository(rawgGamesApiService)
    }
}
