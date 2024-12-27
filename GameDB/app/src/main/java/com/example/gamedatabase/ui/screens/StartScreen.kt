package com.example.gamedatabase.screens.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.gamedatabase.R
import com.example.gamedatabase.data.GamesRepository
import com.example.gamedatabase.model.GamesInList
import com.example.gamedatabase.model.Platform
import com.example.gamedatabase.model.PlatformSpec
import com.example.gamedatabase.ui.RawgTopAppBar
import com.example.gamedatabase.ui.screens.CombinedViewModel
import com.example.gamedatabase.ui.screens.GamesUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(rawgViewModel: CombinedViewModel,
               gamesUiState: GamesUiState,
               retryAction: () -> Unit,
               onLoadMore: () -> Unit,
               onGameClick: (Int) -> Unit,
               onSearch: (String, List<Int>, List<String>) -> Unit, // Modifié pour inclure les plateformes
               onTitleClick: () -> Unit,
               modifier: Modifier = Modifier,
               contentPadding: PaddingValues = PaddingValues(0.dp),
               onFavoriteClick: (Int) -> Unit
) {
    Column {
        RawgTopAppBar(
            scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
            onTitleClick = { onTitleClick() }
        )

        // Remplacez l'ancienne barre de recherche par la nouvelle
        SearchBarWithFilters(onSearch = onSearch)

        // Affiche le contenu en fonction de l'état
        when (gamesUiState) {
            is GamesUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
            is GamesUiState.Success -> GamesListScreen(
                games = gamesUiState.games,
                contentPadding = contentPadding,
                modifier = modifier,
                onLoadMore = onLoadMore,
                onGameClick = onGameClick,
                onFavoriteClick = onFavoriteClick,
                rawgViewModel = rawgViewModel
            )
            is GamesUiState.Error -> ErrorScreen(retryAction, modifier = modifier.fillMaxSize())
        }
    }
}


/**
 * The home screen displaying the loading message.
 */
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

/**
 * The home screen displaying error message with re-attempt button.
 */
@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarWithFilters(
    onSearch: (String, List<Int>, List<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchText by remember { mutableStateOf("") }

    // États pour les plateformes et genres
    var selectedPlatform by remember { mutableStateOf<Int?>(null) }
    val selectedGenres = remember { mutableStateListOf<String>() }

    var expandedPlatform by remember { mutableStateOf(false) }
    var expandedGenres by remember { mutableStateOf(false) }

    // Données des plateformes et genres
    val platforms = listOf(
        "PC" to 4,
        "PlayStation 5" to 187,
        "Xbox One" to 1,
        "Nintendo Switch" to 7
    )

    val genres = listOf("Action", "Adventure", "RPG", "Shooter", "Puzzle", "Strategy", "Sports", "Racing")

    Column(modifier = modifier.padding(8.dp)) {
        // Barre de recherche principale et bouton de recherche
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("Search...") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            Button(
                onClick = {
                    val platformList = selectedPlatform?.let { listOf(it) } ?: emptyList()
                    onSearch(searchText, platformList, selectedGenres)
                },
                modifier = Modifier.height(56.dp)
            ) {
                Text("Search")
            }
        }

        // Menu déroulant pour plateformes et genres sur la même ligne
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Menu déroulant pour les plateformes
            ExposedDropdownMenuBox(
                expanded = expandedPlatform,
                onExpandedChange = { expandedPlatform = it }
            ) {
                TextField(
                    value = platforms.firstOrNull { it.second == selectedPlatform }?.first ?: "Select Platform",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPlatform) },
                    modifier = Modifier
                        .menuAnchor()
                        .width(190.dp) // Ajuste la largeur
                )

                ExposedDropdownMenu(
                    expanded = expandedPlatform,
                    onDismissRequest = { expandedPlatform = false }
                ) {
                    platforms.forEach { (platformName, platformId) ->
                        DropdownMenuItem(
                            text = { Text(platformName) },
                            onClick = {
                                selectedPlatform = platformId
                                expandedPlatform = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp)) // Espacement entre les deux menus

            // Menu déroulant pour les genres
            ExposedDropdownMenuBox(
                expanded = expandedGenres,
                onExpandedChange = { expandedGenres = it }
            ) {
                TextField(
                    value = "Select Genres",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGenres) },
                    modifier = Modifier
                        .menuAnchor()
                        .weight(0.5f) // Ajuste la largeur
                )

                ExposedDropdownMenu(
                    expanded = expandedGenres,
                    onDismissRequest = { expandedGenres = false }
                ) {
                    genres.forEach { genre ->
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = selectedGenres.contains(genre),
                                        onCheckedChange = null
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(genre)
                                }
                            },
                            onClick = {
                                if (selectedGenres.contains(genre)) {
                                    selectedGenres.remove(genre)
                                } else {
                                    selectedGenres.add(genre)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GamesFavoriteScreen(
    games: List<GamesInList>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onGameClick: (Int) -> Unit,
    onFavoriteClick: (Int) -> Unit,
    rawgViewModel: CombinedViewModel
) {
    Column(
        modifier = modifier.padding(horizontal = 8.dp)
    ) {
        Spacer(modifier = Modifier.height(90.dp)) // Ajoute un espace manuel
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(games) { game ->
                GameCardItem(
                    game,
                    onClick = { onGameClick(game.id) },
                    onFavoriteClick = onFavoriteClick,
                    rawgViewModel = rawgViewModel
                )
            }
        }
    }
}

@Composable
fun GamesListScreen(
    games: List<GamesInList>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onLoadMore: () -> Unit,
    onGameClick: (Int) -> Unit,
    onFavoriteClick: (Int) -> Unit,
    rawgViewModel: CombinedViewModel
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(0.dp),
            modifier = modifier.padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(games) { game ->
                GameCardItem(
                    game, onClick = { onGameClick(game.id) },
                    modifier = Modifier,
                    onFavoriteClick = onFavoriteClick,
                    rawgViewModel = rawgViewModel
                )
            }
            // Item sentinel pour charger plus de jeux
            item {
                LaunchedEffect(Unit) {
                    onLoadMore()
                }
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    strokeWidth = 4.dp
                )
            }
        }
        // Bouton flottant pour remonter en haut
        FloatingActionButton(
            onClick = {
                coroutineScope.launch {
                    // Défilement jusqu'en haut
                    listState.animateScrollToItem(0)
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(60.dp),
            containerColor = Color.Black,
        ) {
            Box(
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_up),
                    contentDescription = "Scroll to Top",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun GameCardItem(game: GamesInList, modifier: Modifier = Modifier, onClick: () -> Unit,
                 onFavoriteClick: (Int) -> Unit, rawgViewModel: CombinedViewModel) {
    val isFavorite = rawgViewModel.favoriteStates[game.id] ?: false
    Card(
        modifier = modifier.clickable(onClick = onClick), // Gestion du clic
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            AsyncImage(
                model = game.background_image,
                error = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.loading_img),
                contentDescription = game.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Text(
                text = game.name,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 20.sp,
                    shadow = Shadow(
                        color = Color.Black,
                        offset = Offset(2f, 2f),
                        blurRadius = 4f
                    )
                ),
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            var platformsList = ""
            game.platforms?.forEach { platform ->
                platformsList += platform.platform.name + ", "
            }
            if (platformsList.endsWith(", ")) {
                platformsList = platformsList.substring(0, platformsList.length - 2)
            }
            Row (
                modifier = Modifier.fillMaxWidth().padding(horizontal = 120.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ){
                if (platformsList.contains("PC")){
                    Icon(
                        painter = painterResource(R.drawable.logo_pc),
                        contentDescription = "PC",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(20.dp)
                    )
                }
                if (platformsList.contains("PlayStation 5") || platformsList.contains("PlayStation 4")){
                    Icon(
                        painter = painterResource(R.drawable.logo_playsatation),
                        contentDescription = "Playstation",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(20.dp)
                    )
                }
                if (platformsList.contains("Xbox Series S/X") || platformsList.contains("Xbox One")){
                    Icon(
                        painter = painterResource(R.drawable.logo_xbox),
                        contentDescription = "Xbox",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(20.dp)
                    )
                }
                if (platformsList.contains("Nintendo Switch")){
                    Icon(
                        painter = painterResource(R.drawable.logo_switch),
                        contentDescription = "Nitendo Switch",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    painter = painterResource(R.drawable.calendar),
                    contentDescription = "Date de sortie",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = game.released.toString(),
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f)
                )

                //Row(verticalAlignment = Alignment.CenterVertically) {
                val grade: String = if (game.metacritic == null){
                    "--"
                } else{
                    game.metacritic.toString()
                }
                Text(
                    text = "$grade/100",
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary),
                    textAlign = TextAlign.End
                )
                IconButton(onClick = { onFavoriteClick(game.id) }) {
                    if (isFavorite){
                        Icon(
                            painter = painterResource(R.drawable.star_full_yellow),
                            contentDescription = "Add to Favorites",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    else{
                        Icon(
                            painter = painterResource(R.drawable.star_empty),
                            contentDescription = "Add to Favorites",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                //}
            }
        }
    }
}


