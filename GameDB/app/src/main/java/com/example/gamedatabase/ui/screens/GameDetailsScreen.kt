package com.example.gamedatabase.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.gamedatabase.ui.LoadingScreen
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.gamedatabase.ui.ErrorScreen

@Composable
fun GameDetailsScreen(gameId: Int, modifier: Modifier = Modifier) {
    val viewModel: CombinedViewModel = viewModel(factory = CombinedViewModel.Factory)
    viewModel.getGameDetails(gameId)

    when (viewModel.gameDetailsUiState) {
        is GameDetailsUiState.Loading -> LoadingScreen(modifier = modifier)
        is GameDetailsUiState.Success -> {
            val gameDetails = (viewModel.gameDetailsUiState as GameDetailsUiState.Success).gameDetails
            Column(modifier = modifier) {
                AsyncImage(
                    model = gameDetails.background_image,
                    contentDescription = gameDetails.name,
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = gameDetails.name,
                    style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.onBackground) // Texte en blanc
                )
                Text(
                    text = gameDetails.description ?: "No description available",
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground) // Texte en blanc
                )
            }
        }
        is GameDetailsUiState.Error -> ErrorScreen(
            retryAction = { viewModel.getGameDetails(gameId) },
            modifier = modifier
        )
    }
}

