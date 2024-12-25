@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.gamedatabase.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gamedatabase.R
import com.example.gamedatabase.data.AppDatabase
import com.example.gamedatabase.data.OfflineUserRepository
import com.example.gamedatabase.data.UserRepository
import com.example.gamedatabase.screens.ui.HomeScreen
import com.example.gamedatabase.ui.screens.CombinedViewModel
import com.example.gamedatabase.ui.screens.FavoritesScreen
import com.example.gamedatabase.ui.screens.GameDetailsScreen
import com.example.gamedatabase.ui.screens.LoginScreen
import com.example.gamedatabase.ui.screens.RandomGameDetailsScreenWithShake
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RawgApp() {
    val navController = rememberNavController()
    val rawgViewModel: CombinedViewModel = viewModel(factory = CombinedViewModel.Factory)
    val userRepository: UserRepository = OfflineUserRepository(AppDatabase.getDatabase(LocalContext.current).userDao())

    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.padding(16.dp)) {
                    TextButton(
                        onClick = {
                            scope.launch {
                                drawerState.close()
                            }
                            navController.navigate("gameDetails/random") // Navigue vers un jeu aléatoire
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Shake Game", style = MaterialTheme.typography.bodyLarge)
                    }
                    TextButton(
                        onClick = {
                            scope.launch {
                                drawerState.close()
                            }
                            navController.navigate("favoris") // Navigue vers l'accueil
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Favoris", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    ) {
        NavHost(navController = navController, startDestination = "home") {
            // Login screen
            composable("login") {
                LoginScreen(
                    onLoginSuccess = { navController.navigate("home") { popUpTo("login") { inclusive = true } } },
                    onSignUp = { /* Implémenter un écran d'inscription si nécessaire */ },
                    userRepository = userRepository,
                    rawgViewModel = rawgViewModel
                )
            }
            // Home screen
            composable("home") {
                Scaffold(
                    topBar = {
                        RawgTopAppBarWithMenu(
                            scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                            onMenuClick = { scope.launch {
                                drawerState.open()
                            }  },
                            onTitleClick = {
                                rawgViewModel.loadOriginalGames() // Charger les jeux d'origine
                                navController.popBackStack("home", inclusive = false)
                            }
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
                        onSearch = { query, selectedPlatforms, selectedGenres ->
                            rawgViewModel.searchGames(query, selectedPlatforms, selectedGenres)
                        },
                        onTitleClick = { navController.navigate("home") },
                        contentPadding = innerPadding,
                        onFavoriteClick = { gameId ->
                            rawgViewModel.toggleGameInFavorites(
                                gameId,
                                userRepository
                            )
                        },
                        rawgViewModel = rawgViewModel
                    )
                }
            }

            //Favorites
            composable("favoris") {
                Scaffold(
                    topBar = {
                        RawgTopAppBarWithMenu(
                            scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                            onMenuClick = { scope.launch {
                                drawerState.open()
                            }  },
                            onTitleClick = {
                                rawgViewModel.loadOriginalGames() // Charger les jeux d'origine
                                navController.popBackStack("home", inclusive = false)
                            }
                        )
                    }
                ) { innerPadding ->
                    FavoritesScreen(
                        userRepository = userRepository,
                        rawgViewModel = rawgViewModel,
                        contentPadding = innerPadding,
                        onLoadMore = { gameId ->
                            navController.navigate("gameDetails/$gameId")
                        },
                        onGameClick = { gameId ->
                            navController.navigate("gameDetails/$gameId")
                        },
                        onFavoriteClick = { gameId ->
                            rawgViewModel.toggleGameInFavorites(
                                gameId,
                                userRepository
                            )
                        }
                    )
                }
            }


            // Game details screen
            composable("gameDetails/{gameId}") { backStackEntry ->
                val gameId = backStackEntry.arguments?.getString("gameId")?.toInt() ?: 0
                Scaffold(
                    topBar = {
                        RawgTopAppBarWithMenu(
                            scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                            onMenuClick = { scope.launch {
                                drawerState.open()
                            }  },
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

            // Random game details
            composable("gameDetails/random") {
                // Logique pour naviguer vers un jeu aléatoire
                Scaffold(
                    topBar = {
                        RawgTopAppBarWithMenu(
                            scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                            onMenuClick = { scope.launch {
                                drawerState.open()
                            }  },
                            onTitleClick = { navController.navigate("home") }
                        )
                    }
                ) { innerPadding ->
                    RandomGameDetailsScreenWithShake(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RawgTopAppBarWithMenu(
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit,
    onTitleClick: (() -> Unit)? = null
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(R.string.menu),
                    tint = Color.White
                )
            }
        },
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
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








