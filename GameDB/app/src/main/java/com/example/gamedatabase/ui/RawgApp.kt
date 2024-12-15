@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.gamedatabase.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
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
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            val rawgViewModel: CombinedViewModel = viewModel(factory = CombinedViewModel.Factory)
            Scaffold(
                topBar = {
                    RawgTopAppBar(
                        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                        showBackButton = false // Pas de bouton retour sur la page d'accueil
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
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
        composable("gameDetails/{gameId}") { backStackEntry ->
            val gameId = backStackEntry.arguments?.getString("gameId")?.toInt() ?: 0
            Scaffold(
                topBar = {
                    RawgTopAppBar(
                        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                        showBackButton = true, // Affiche le bouton retour
                        onBackClick = { navController.popBackStack() } // Navigue en arrière
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
    showBackButton: Boolean = false, // Paramètre pour afficher la flèche
    onBackClick: (() -> Unit)? = null // Action de retour
) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (showBackButton) { // Affiche la flèche si nécessaire
                IconButton(onClick = { onBackClick?.invoke() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Icône de retour
                        contentDescription = stringResource(R.string.back),
                        tint = Color.White
                    )
                }
            }
        },
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineSmall.copy(color = Color.White),
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Black
        ),
        modifier = modifier
    )
}



