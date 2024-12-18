package com.example.gamedatabase.ui.screens

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.gamedatabase.ui.LoadingScreen
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.gamedatabase.R
import com.example.gamedatabase.ui.ErrorScreen
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

@Composable
fun RandomGameDetailsScreen(modifier: Modifier = Modifier) {
    val viewModel: CombinedViewModel = viewModel(factory = CombinedViewModel.Factory)

    // Génère un identifiant aléatoire une seule fois
    val randomGameId = remember { (0..5000).random() }

    // Variable mutable pour stocker l'ID aléatoire, initialisée avec `randomGameId`
    val currentGameId = remember { mutableStateOf(randomGameId) }

    // Charge les détails du jeu pour cet identifiant
    LaunchedEffect(currentGameId.value) {
        viewModel.getGameDetails(currentGameId.value)
    }

    when (val state = viewModel.gameDetailsUiState) {
        is GameDetailsUiState.Loading -> LoadingScreen(modifier = modifier)
        is GameDetailsUiState.Success -> {
            val gameDetails = state.gameDetails
            Column(
                modifier = modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Image de fond
                AsyncImage(
                    model = gameDetails.background_image,
                    error = painterResource(R.drawable.ic_broken_image),
                    contentDescription = gameDetails.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                // Nom du jeu
                Text(
                    text = gameDetails.name,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                // Description
                Text(
                    text = decodeHtml(gameDetails.description.toString()) ?: "No description available",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Détails supplémentaires
                Text(
                    text = "Released: ${gameDetails.released}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = "Metacritic Score: ${gameDetails.metacritic}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        is GameDetailsUiState.Error -> {
//            // Générer un nouvel ID et relancer la requête
//            currentGameId.value = (0..5000).random()
            // Affiche l'écran d'erreur avec une action de retry
            ErrorScreen(
                retryAction = {
                    val newRandomGameId = (0..5000).random() // Générer un nouvel ID uniquement lors du retry
                    viewModel.getGameDetails(newRandomGameId)
                },
                modifier = modifier
            )
        }
    }
}

@Composable
fun RandomGameDetailsScreenWithShake(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewModel: CombinedViewModel = viewModel(factory = CombinedViewModel.Factory)

    // Variable mutable pour stocker l'ID aléatoire
    val currentGameId = remember { mutableStateOf((0..5000).random()) }

    // Charge les détails du jeu pour cet identifiant
    LaunchedEffect(currentGameId.value) {
        viewModel.getGameDetails(currentGameId.value)
    }

    // Gérer la détection de secousse
    ShakeDetector(context) {
        // Mettre à jour l'identifiant et relancer la requête
        currentGameId.value = (0..5000).random()
    }

    when (val state = viewModel.gameDetailsUiState) {
        is GameDetailsUiState.Loading -> LoadingScreen(modifier = modifier)
        is GameDetailsUiState.Success -> {
            val gameDetails = state.gameDetails
            Column(
                modifier = modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Image de fond
                AsyncImage(
                    model = gameDetails.background_image,
                    error = painterResource(R.drawable.ic_broken_image),
                    contentDescription = gameDetails.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                // Nom du jeu
                Text(
                    text = gameDetails.name,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                // Description
                Text(
                    text = decodeHtml(gameDetails.description.toString()) ?: "No description available",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Détails supplémentaires
                Text(
                    text = "Released: ${gameDetails.released}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = "Metacritic Score: ${gameDetails.metacritic}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        is GameDetailsUiState.Error -> {
            // Générer un nouvel ID en cas d'erreur
            currentGameId.value = (0..5000).random()
        }
    }
}

/**
 * Utilitaire pour détecter une secousse.
 */
@Composable
fun ShakeDetector(context: Context, onShake: () -> Unit) {
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    val accelerometer = remember { sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }
    val shakeThreshold = 25.0f
    val lastUpdate = remember { mutableStateOf(System.currentTimeMillis()) }

    DisposableEffect(Unit) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]

                    val acceleration = Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
                    val now = System.currentTimeMillis()

                    if (acceleration > shakeThreshold && now - lastUpdate.value > 500) {
                        lastUpdate.value = now
                        onShake()
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_UI)

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }
}






