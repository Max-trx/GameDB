package com.example.gamedatabase.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import com.example.gamedatabase.data.UserRepository
import com.example.gamedatabase.model.GamesInList
import com.example.gamedatabase.screens.ui.GamesFavoriteScreen

@Composable
fun FavoritesScreen(
    userRepository: UserRepository,
    rawgViewModel: CombinedViewModel,
    onLoadMore: (Any?) -> Unit,
    onGameClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    onFavoriteClick: (Int) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
                    ) {
    val context = LocalContext.current
    val userId = rawgViewModel.loggedInUserId
    val favoriteGames = remember { mutableStateOf<List<GamesInList>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(userId) {
        userId?.let { id ->
            val user = userRepository.getUserById(id)
            user?.favGames?.split(",")?.filter { it.isNotEmpty() }?.map { it.toInt() }?.let { favoriteIds ->
                val games = mutableListOf<GamesInList>()
                favoriteIds.forEach { gameId ->
                    try {
                        val gameDetails = rawgViewModel.gamesRepository.getGameDetails(gameId, "3e0805133d704bd0b792f417960f423c")
                        games.add(
                            GamesInList(
                                id = gameDetails.id,
                                name = gameDetails.name,
                                background_image = gameDetails.background_image,
                                released = gameDetails.released,
                                metacritic = gameDetails.metacritic
                            )
                        )
                    } catch (e: Exception) {
                        Log.e("FavoritesScreen", "Error fetching game details: $e")
                    }
                }
                favoriteGames.value = games
            }
        }
    }
    GamesFavoriteScreen(
        games = favoriteGames.value,
        contentPadding = contentPadding,
        modifier = modifier,
        onGameClick = onGameClick,
        onFavoriteClick = onFavoriteClick,
        rawgViewModel = rawgViewModel
    )
}
