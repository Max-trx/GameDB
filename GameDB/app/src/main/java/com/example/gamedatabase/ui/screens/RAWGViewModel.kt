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
import com.example.gamedatabase.RawgApplication
import com.example.gamedatabase.data.GamesRepository
import com.example.gamedatabase.model.Games
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface GamesUiState {
    data class Success(val games: List<Games>) : GamesUiState
    object Error : GamesUiState
    object Loading : GamesUiState
}

class RAWGViewModel(private val gamesRepository: GamesRepository) : ViewModel() {
    var gamesUiState: GamesUiState by mutableStateOf(GamesUiState.Loading)
        private set

    init {
        getGames("3e0805133d704bd0b792f417960f423c") // Requête initiale
    }

    fun getGames(query: String) {
        viewModelScope.launch {
            gamesUiState = GamesUiState.Loading
            gamesUiState = try {
                GamesUiState.Success(gamesRepository.getGames(query))
            } catch (e: IOException) {
                GamesUiState.Error
            } catch (e: HttpException) {
                GamesUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as RawgApplication)
                val gamesRepository = application.container.gamesRepository
                RAWGViewModel(gamesRepository = gamesRepository)
            }
        }
    }
}