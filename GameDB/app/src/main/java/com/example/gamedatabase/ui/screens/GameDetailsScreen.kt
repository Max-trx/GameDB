package com.example.gamedatabase.ui.screens

import android.text.Html
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamedatabase.model.GameDetails
import com.example.gamedatabase.model.Platform
import com.example.gamedatabase.screens.ui.ErrorScreen
import com.example.gamedatabase.screens.ui.LoadingScreen

@Composable
fun GameDetailsScreen(gameId: Int, modifier: Modifier = Modifier) {
    val viewModel: CombinedViewModel = viewModel(factory = CombinedViewModel.Factory)
    viewModel.getGameDetails(gameId)

    when (viewModel.gameDetailsUiState) {
        is GameDetailsUiState.Loading -> LoadingScreen(modifier = modifier)
        is GameDetailsUiState.Success -> {
            val gameDetails = (viewModel.gameDetailsUiState as GameDetailsUiState.Success).gameDetails
            Column(
                modifier = modifier
                    .padding(16.dp) // Ajouter un peu de marge autour de l'écran
                    .verticalScroll(rememberScrollState()) // Permet de faire défiler le contenu si nécessaire
            ) {
                // Image de fond
                AsyncImage(
                    model = gameDetails.background_image,
                    contentDescription = gameDetails.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                // Nom du jeu avec un titre plus grand et une ombre
                Text(
                    text = gameDetails.name,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        shadow = Shadow(
                            color = Color.Gray,
                            offset = Offset(2f, 2f),
                            blurRadius = 4f
                        )
                    ),
                    modifier = Modifier
                        .padding(top = 16.dp) // Ajouter un espace au-dessus
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Release Date
                Text(
                    text = "Released: ${gameDetails.released}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                //Grade
                val grade: String = if (gameDetails.metacritic == null){"Not exist"}
                else {gameDetails.metacritic.toString()+"/100"}
                Text(
                    text = "Metacritic Score: $grade",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Rating by players: ${gameDetails.rating}/5 evaluated by ${gameDetails.ratings_count} players",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Average playtime on this game: ${gameDetails.playtime} hours",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
                var platformsList = "" // Assurez-vous d'initialiser la chaîne
                gameDetails.platforms?.forEach { platform ->
                    platformsList += platform.platform.name + ", " // Ajoutez une virgule pour séparer les plateformes
                }
                if (platformsList.endsWith(", ")) {
                    platformsList = platformsList.substring(0, platformsList.length - 2)
                }
                Text(
                    text = "Platforms: $platformsList", // Utilisez l'interpolation de chaîne
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                //Genre
                Spacer(modifier = Modifier.height(16.dp))
                var genreList = ""
                gameDetails.genres?.forEach { genre ->
                    genreList += genre.name + ", "
                }
                if (genreList.endsWith(", ")){
                    genreList = genreList.substring(0, genreList.length - 2)
                }
                Text(
                    text = "Genre(s): $genreList", // Utilisez l'interpolation de chaîne
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                // Description du jeu
                Text(
                    text = ("Description: \n" + decodeHtml(gameDetails.description.toString()))
                        ?: "No description available",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier
                        .padding(top = 8.dp) // Espace entre le titre et la description
                        .fillMaxWidth()
                )



                // Espacement avant de finir
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        is GameDetailsUiState.Error -> ErrorScreen(
            retryAction = { viewModel.getGameDetails(gameId) },
            modifier = modifier
        )
    }
}

fun decodeHtml(html: String): String {
    return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString()
}



@Preview
@Composable
fun PreviewGameDetailsScreen() {
    val sampleGameDetails = GameDetails(
        id = 1,
        name = "Sample Game",
        background_image = "url_to_image",
        description = "This is a sample game description."
    )

    val gameDetailsUiState = GameDetailsUiState.Success(gameDetails = sampleGameDetails)


    GameDetailsScreen(gameId = 1)
}



