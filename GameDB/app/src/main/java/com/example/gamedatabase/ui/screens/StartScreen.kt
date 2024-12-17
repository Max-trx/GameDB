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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.gamedatabase.R
import com.example.gamedatabase.model.GamesInList
import com.example.gamedatabase.ui.screens.GamesUiState


//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun HomeScreen(
//    gamesUiState: GamesUiState,
//    retryAction: () -> Unit,
//    onLoadMore: () -> Unit,
//    onGameClick: (Int) -> Unit,
//    onSearch: (String) -> Unit, // Ajout du paramètre pour la recherche
//    onTitleClick: () -> Unit,  // Navigue vers l'écran d'accueil
//    modifier: Modifier = Modifier,
//    contentPadding: PaddingValues = PaddingValues(0.dp),
//) {
//    Column {
//        // Affiche le titre de l'application
//        RawgTopAppBar(
//            scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
//            onTitleClick = onTitleClick // Clic renvoie à l'accueil
//        )
//
//        // Affiche la barre de recherche
//        SearchBar(onSearch = onSearch)
//
//        // Affiche le contenu en fonction de l'état
//        when (gamesUiState) {
//            is GamesUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
//            is GamesUiState.Success -> GamesListScreen(
//                games = gamesUiState.games,
//                contentPadding = contentPadding,
//                modifier = modifier,
//                onLoadMore = onLoadMore,
//                onGameClick = onGameClick
//            )
//            is GamesUiState.Error -> ErrorScreen(retryAction, modifier = modifier.fillMaxSize())
//        }
//    }
//}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    gamesUiState: GamesUiState,
    retryAction: () -> Unit,
    onLoadMore: () -> Unit,
    onGameClick: (Int) -> Unit,
    onSearch: (String, List<Int>) -> Unit, // Modifié pour inclure les plateformes
    onTitleClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
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
                onGameClick = onGameClick
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

@Composable
fun SearchBar(onSearch: (String) -> Unit, modifier: Modifier = Modifier) {
    var searchText by remember { mutableStateOf("") }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
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
            onClick = { onSearch(searchText) }, // Passe le texte à la fonction de recherche
            modifier = Modifier.height(56.dp)
        ) {
            Text("Search")
        }
    }
}

@Composable
fun SearchBarWithFilters(onSearch: (String, List<Int>) -> Unit, modifier: Modifier = Modifier) {
    var searchText by remember { mutableStateOf("") }
    val selectedPlatforms = remember { mutableStateListOf<Int>() }
    var showFilters by remember { mutableStateOf(false) } // État pour contrôler la visibilité des filtres

    Column(modifier = modifier.padding(8.dp)) {
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
                onClick = { onSearch(searchText, selectedPlatforms) },
                modifier = Modifier.height(56.dp)
            ) {
                Text("Search")
            }
        }

        // Flèche pour afficher/masquer les filtres
        IconButton(onClick = { showFilters = !showFilters }) {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Toggle Filters"
            )
        }

        // Afficher les filtres si showFilters est vrai
        if (showFilters) {
            Column {
                val platforms = listOf(
                    "PC" to 4,
                    "PlayStation 5" to 187,
                    "Xbox One" to 1,
                    "Nintendo Switch" to 7
                )
                platforms.forEach { (platformName, platformId) ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = selectedPlatforms.contains(platformId),
                            onCheckedChange = { checked ->
                                if (checked) {
                                    selectedPlatforms.add(platformId)
                                } else {
                                    selectedPlatforms.remove(platformId)
                                }
                            }
                        )
                        Text(text = platformName)
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
    onGameClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 8.dp),
        contentPadding = PaddingValues(0.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(games) { game ->
            GameCardItem(game, onClick = { onGameClick(game.id) })
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
fun GameCardItem(games: GamesInList, modifier: Modifier = Modifier, onClick: () -> Unit) {
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
                    text = games.released.toString(),
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = (games.metacritic.toString()+"/100"),
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
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun HomeScreenPreview() {
//    // On crée une version simplifiée de l'état des jeux pour prévisualisation
//    val mockGamesUiState = GamesUiState.Success(
//        games = listOf(
//            GamesInList(id = 1, name = "Game 1", background_image = "https://example.com/image1.jpg"),
//            GamesInList(id = 2, name = "Game 2", background_image = "https://example.com/image2.jpg"),
//            GamesInList(id = 3, name = "Game 3", background_image = "https://example.com/image3.jpg")
//        )
//    )
//
//    HomeScreen(
//        gamesUiState = mockGamesUiState,
//        retryAction = {},
//        onLoadMore = {},
//        onGameClick = {},
//        onSearch = {},
//        onTitleClick = {},
//        modifier = Modifier.fillMaxSize()
//    )
//}







