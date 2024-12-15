@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.gamedatabase.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gamedatabase.R
import com.example.gamedatabase.data.AppContainer
import com.example.gamedatabase.ui.screens.RAWGViewModel
import com.example.gamedb.HomeScreen


@Composable
fun RawgApp() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { RawgTopAppBar(scrollBehavior = scrollBehavior) }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black
        ) {
            // Utilisez le Factory pour créer le ViewModel
            val rawgViewModel: RAWGViewModel = viewModel(factory = RAWGViewModel.Factory)
            HomeScreen(
                gamesUiState = rawgViewModel.gamesUiState,
                retryAction = { rawgViewModel.getGames(rawgViewModel.currentPage) },
                onLoadMore = { rawgViewModel.loadMoreGames() }, // Ajoutez cette ligne pour charger plus de jeux
                contentPadding = it
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RawgTopAppBar(scrollBehavior: TopAppBarScrollBehavior, modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineSmall.copy(color = Color.White), // Texte blanc
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Black // Fond noir pour la barre
        ),
        modifier = modifier
    )
}

