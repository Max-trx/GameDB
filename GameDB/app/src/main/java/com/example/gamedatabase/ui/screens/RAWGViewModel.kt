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
import com.example.gamedatabase.RawgApplication
import com.example.gamedatabase.data.GamesRepository
import com.example.gamedatabase.model.GamesInList
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface GamesUiState {
    data class Success(val games: List<GamesInList>) : GamesUiState
    object Error : GamesUiState
    object Loading : GamesUiState
}

class RAWGViewModel(private val gamesRepository: GamesRepository) : ViewModel() {
    var gamesUiState: GamesUiState by mutableStateOf(GamesUiState.Loading)
        private set

    var currentPage = 1

    init {
        getGames(currentPage) // Requête initiale
    }

    fun getGames(page: Int) {
        viewModelScope.launch {
            try {
                val newGames = gamesRepository.getGames("3e0805133d704bd0b792f417960f423c", page)
                // Mettez à jour l'état avec les nouveaux jeux
                if (gamesUiState is GamesUiState.Success) {
                    val currentGames = (gamesUiState as GamesUiState.Success).games
                    gamesUiState = GamesUiState.Success(currentGames + newGames)
                    Log.d("debug", "gamesUiState is GamesUiState.Success")
                } else {
                    Log.d("debug", "gamesUiState is NOT GamesUiState.Success")
                    gamesUiState = GamesUiState.Success(newGames)
                }
            } catch (e: IOException) {
                gamesUiState = GamesUiState.Error
            } catch (e: HttpException) {
                gamesUiState = GamesUiState.Error
            }
        }
    }

    fun loadMoreGames() {
        currentPage++
        getGames(currentPage)
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