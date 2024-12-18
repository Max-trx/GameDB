package com.example.gamedatabase.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.gamedatabase.data.GamesRepository
import com.example.gamedatabase.model.GameDetails
import com.example.gamedatabase.model.GamesInList
import com.example.gamedatabase.RawgApplication
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface GamesUiState {
    data class Success(val games: List<GamesInList>) : GamesUiState
    object Error : GamesUiState
    object Loading : GamesUiState
}

sealed interface GameDetailsUiState {
    data class Success(val gameDetails: GameDetails) : GameDetailsUiState
    object Error : GameDetailsUiState
    object Loading : GameDetailsUiState
}

class CombinedViewModel(private val gamesRepository: GamesRepository) : ViewModel() {
    var gamesUiState: GamesUiState by mutableStateOf(GamesUiState.Loading)
        private set

    var gameDetailsUiState: GameDetailsUiState by mutableStateOf(GameDetailsUiState.Loading)
        private set

    var currentPage = 1
    private var isLoading = false // Empêche le double chargement

    init {
        getGames(currentPage) // Requête initiale
    }

    fun getGames(page: Int) {
        if (isLoading) return // Empêche les appels multiples
        isLoading = true

        viewModelScope.launch {
            try {
                val newGames = gamesRepository.getGames("3e0805133d704bd0b792f417960f423c", page)
                if (gamesUiState is GamesUiState.Success) {
                    val currentGames = (gamesUiState as GamesUiState.Success).games
                    gamesUiState = GamesUiState.Success(currentGames + newGames)
                } else {
                    gamesUiState = GamesUiState.Success(newGames)
                }
            } catch (e: IOException) {
                gamesUiState = GamesUiState.Error
            } catch (e: HttpException) {
                gamesUiState = GamesUiState.Error
            } finally {
                isLoading = false
            }
        }
    }

    fun loadMoreGames() {
        currentPage++
        getGames(currentPage)
    }

    fun getGameDetails(gameId: Int) {
        viewModelScope.launch {
            try {
                val gameDetails = gamesRepository.getGameDetails(gameId, "3e0805133d704bd0b792f417960f423c")
                gameDetailsUiState = GameDetailsUiState.Success(gameDetails)
            } catch (e: IOException) {
                gameDetailsUiState = GameDetailsUiState.Error
            } catch (e: HttpException) {
                gameDetailsUiState = GameDetailsUiState.Error
            }
        }
    }

    // Méthode pour charger les jeux d'origine
    fun loadOriginalGames() {
        viewModelScope.launch {
            try {
                val originalGames = gamesRepository.getGames("3e0805133d704bd0b792f417960f423c", 1)
                gamesUiState = GamesUiState.Success(originalGames)
            } catch (e: Exception) {
                gamesUiState = GamesUiState.Error
            }
        }
    }

    fun searchGames(query: String, selectedPlatforms: List<Int>, selectedGenres: List<String>) {
        if (isLoading) return
        isLoading = true

        viewModelScope.launch {
            try {
                val platformQuery = if (selectedPlatforms.isNotEmpty()) {
                    selectedPlatforms.joinToString("&") // Convertir la liste d'IDs en chaîne, séparée par des virgules
                } else {
                    null
                }

                val genresQuery = if (selectedGenres.isNotEmpty()) {
                    selectedGenres.joinToString(",").lowercase() // Convertir la liste d'IDs en chaîne, séparée par des virgules
                } else {
                    null
                }
                val apiKey = "3e0805133d704bd0b792f417960f423c"
                val searchResults = when {
                    // query + platform + genre
                    query.isNotEmpty() && platformQuery != null && genresQuery != null-> {
                        gamesRepository.getGameSearchPlatformGenre(apiKey, query, platformQuery, genresQuery)
                    }
                    // query + platform
                    query.isNotEmpty() && platformQuery != null ->{
                        gamesRepository.getGameSearchPlatform(apiKey, query, platformQuery)
                    }
                    // platform + genre
                    platformQuery != null && genresQuery != null->{
                        gamesRepository.getGameSearchGenre(apiKey, platformQuery, genresQuery)
                    }
                    // Only query
                    query.isNotEmpty() -> {
                        // Utiliser getGameSearch si seule la requête de recherche est fournie
                        gamesRepository.getGameSearch(apiKey, query)
                    }
                    // Only platform
                    platformQuery != null -> {
                        // Utiliser getGameFilter si seule la plateforme est fournie
                        gamesRepository.getGamePlatform(apiKey, platformQuery)
                    }
                    // Only genres
                    genresQuery != null -> {
                        // Utiliser getGameFilter si seule la plateforme est fournie
                        gamesRepository.getGameGenre(apiKey, genresQuery)
                    }
                    // Any
                    else -> {
                        gamesRepository.getGames(apiKey,1)
                    }

                }
                Log.d("debug", "query =$query, platform= $platformQuery, genres= $genresQuery")
                gamesUiState = GamesUiState.Success(searchResults)
            } catch (e: IOException) {
                gamesUiState = GamesUiState.Error
            } catch (e: HttpException) {
                gamesUiState = GamesUiState.Error
            } finally {
                isLoading = false
            }
        }
    }



    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as RawgApplication)
                val gamesRepository = application.container.gamesRepository
                CombinedViewModel(gamesRepository = gamesRepository)
            }
        }
    }
}