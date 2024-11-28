package com.example.gamedb

import androidx.compose.foundation.rememberScrollState
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gamedb.ui.Game
import com.example.gamedb.ui.theme.GameDBTheme

enum class GameAppScreen(@StringRes val title: Int) {
    Home(title = R.string.app_name),
    GameDetails(title = R.string.game_details)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameAppBar(
    @StringRes currentScreenTitle: Int,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(currentScreenTitle)) },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun StartScreen(
    onGameClicked: (Game) -> Unit,
    modifier: Modifier = Modifier
) {
    val games = listOf(
        Game(
            title = "Red Dead Redemption II",
            description = "After a robbery goes badly wrong in the western town of Blackwater...",
            releaseDate = "26 October 2018",
            developers = "Rockstar Games",
            platforms = "PlayStation 4, Xbox One, Google Stadia, Microsoft Windows",
            imageRes = R.drawable.rdr2 // Remplacez par une ressource d'image valide
        )
    )

    LazyColumn(modifier = modifier) {
        items(games) { game ->
            GameItem(game = game, onGameClicked = onGameClicked)
        }
    }
}

@Composable
fun GameItem(game: Game, onGameClicked: (Game) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onGameClicked(game) }
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            Image(
                painter = painterResource(id = game.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = game.title, style = MaterialTheme.typography.headlineLarge)
                Text(text = game.releaseDate, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}


@Composable
fun GameApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = GameAppScreen.valueOf(
        backStackEntry?.destination?.route ?: GameAppScreen.Home.name
    )

    Scaffold(
        topBar = {
            GameAppBar(
                currentScreenTitle = currentScreen.title,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = GameAppScreen.Home.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = GameAppScreen.Home.name) {
                HomeScreen(
                    onGameClicked = { game ->
                        navController.navigate("${GameAppScreen.GameDetails.name}/$game")
                    }
                )
            }
            composable(route = "${GameAppScreen.GameDetails.name}/{gameId}") { backStackEntry ->
                val gameId = backStackEntry.arguments?.getString("gameId")
                GameDetailsScreen(gameId = gameId)
            }
        }
    }
}

@Composable
fun HomeScreen(onGameClicked: (String) -> Unit) {
    val games = listOf("Red Dead Redemption II", "Minecraft", "Apex Legends", "GTA V")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Popular Games",
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        for (game in games.chunked(2)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                game.forEach {
                    GameCard(title = it, onClick = { onGameClicked(it) })
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun GameCard(title: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(160.dp)
            .background(Color.Gray, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun GameDetailsScreen(gameId: String?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = gameId ?: "Unknown Game",
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
            color = Color.White
        )
        Text(
            text = "Description: This is a detailed description of the game $gameId.",
            style = TextStyle(fontSize = 16.sp),
            color = Color.White
        )
        Text(
            text = "Release Date: October 26, 2018",
            style = TextStyle(fontSize = 16.sp),
            color = Color.White
        )
        Text(
            text = "Platforms: PlayStation, Xbox, PC",
            style = TextStyle(fontSize = 16.sp),
            color = Color.White
        )
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
fun AppPreview() {
    GameDBTheme {
        GameApp()
    }
}
