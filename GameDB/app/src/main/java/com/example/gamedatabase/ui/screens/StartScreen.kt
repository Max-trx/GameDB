package com.example.gamedatabase.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.gamedatabase.R
import com.example.gamedatabase.model.GamesInList
import com.example.gamedatabase.ui.screens.CombinedViewModel
import com.example.gamedatabase.ui.screens.GamesUiState

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
                    val genreList = selectedGenres
                    onSearch(searchText, platformList, genreList)
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
fun GamesListScreen(
    games: List<GamesInList>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onLoadMore: () -> Unit,
    onGameClick: (Int) -> Unit,
    onFavoriteClick: (Int) -> Unit,
    rawgViewModel: CombinedViewModel
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 8.dp),
        contentPadding = PaddingValues(0.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(games) { game ->
            GameCardItem(
                game, onClick = { onGameClick(game.id) },
                modifier = modifier,
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
}


@Composable
fun GameCardItem(games: GamesInList, modifier: Modifier = Modifier, onClick: () -> Unit,
                 onFavoriteClick: (Int) -> Unit, rawgViewModel: CombinedViewModel) {
    Card(
        modifier = modifier.clickable(onClick = onClick), // Gestion du clic
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface) // Utilisation de la couleur des surfaces du thème
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            AsyncImage(
                model = games.background_image,
                error = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.loading_img),
                contentDescription = games.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Text(
                text = games.name,
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.calendar),
                    contentDescription = "Date de sortie",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = games.released.toString() ?: "Unknown Date",
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = (((games.metacritic.toString() + "/100") ?: "--/100")),
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary),
                        textAlign = TextAlign.End
                    )
                    Icon(
                        painter = painterResource(R.drawable.star),
                        contentDescription = "Note Metacritic",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(20.dp)
                            .padding(start = 8.dp)
                    )
                    IconButton(onClick = { onFavoriteClick(games.id) }) {
                        Icon(
                            painter = painterResource(R.drawable.star),
                            contentDescription = "Add to Favorites"
                        )
                    }
                }
            }
        }
    }
}







