@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.gamedatabase.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gamedatabase.R
import com.example.gamedatabase.ui.screens.CombinedViewModel
import com.example.gamedatabase.ui.screens.GameDetailsScreen


@Composable
fun RawgApp() {
    val navController = rememberNavController()
    val rawgViewModel: CombinedViewModel = viewModel(factory = CombinedViewModel.Factory)

    NavHost(navController = navController, startDestination = "home") {
        // Home screen
        composable("home") {
            Scaffold(
                // Titre et barre de recherche pour l'accueil
                topBar = {
                    RawgTopAppBar(
                        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                        onTitleClick = { navController.popBackStack("home", inclusive = false) }
                    )
                }
            ) { innerPadding ->
                HomeScreen(
                    gamesUiState = rawgViewModel.gamesUiState,
                    retryAction = { rawgViewModel.getGames(rawgViewModel.currentPage) },
                    onLoadMore = { rawgViewModel.loadMoreGames() },
                    onGameClick = { gameId ->
                        navController.navigate("gameDetails/$gameId")
                    },
                    // Modifiez l'appel ici pour passer une liste vide pour les plateformes
                    onSearch = { query, selectedPlatforms -> rawgViewModel.searchGames(query, selectedPlatforms) },
                    onTitleClick = { navController.navigate("home") }, // Ajout de onTitleClick
                    contentPadding = innerPadding
                )
            }
        }

        // Game details screen
        composable("gameDetails/{gameId}") { backStackEntry ->
            val gameId = backStackEntry.arguments?.getString("gameId")?.toInt() ?: 0
            Scaffold(
                // Seulement le titre et la flèche retour
                topBar = {
                    RawgTopAppBar(
                        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                        showBackButton = true,
                        onBackClick = { navController.popBackStack() },
                        onTitleClick = { navController.navigate("home") }
                    )
                }
            ) { innerPadding ->
                GameDetailsScreen(
                    gameId = gameId,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RawgTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    showBackButton: Boolean = false,
    onBackClick: (() -> Unit)? = null,
    onTitleClick: (() -> Unit)? = null
) {
    TopAppBar(
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = { onBackClick?.invoke() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        tint = Color.White
                    )
                }
            }
        },
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(), // Utilise un Box pour occuper toute la largeur
                contentAlignment = Alignment.Center // Centre le contenu dans le Box
            ) {
                Text(
                    text = "Game DataBase",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.clickable { onTitleClick?.invoke() }
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black),
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun RawgTopAppBarPreview() {
    RawgTopAppBar(
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
        showBackButton = true,
        onBackClick = { /* Action back */ },
        onTitleClick = { /* Action on title click */ }
    )
}








