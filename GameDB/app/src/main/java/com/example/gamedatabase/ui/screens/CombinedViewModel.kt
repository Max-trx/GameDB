package com.example.gamedatabase.ui.screens

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