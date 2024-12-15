package com.example.gamedatabase.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Définir une palette de couleurs sombres
private val DarkColorScheme = darkColorScheme(
    primary = Color.White,        // Couleur principale
    onPrimary = Color.Black,      // Texte ou contenu sur la couleur principale
    background = Color.Black,     // Couleur de fond principale
    surface = Color.DarkGray,     // Couleur des surfaces (ex. : cartes, dialogues)
    onBackground = Color.White,   // Texte ou contenu sur le fond principal
    onSurface = Color.White       // Texte ou contenu sur les surfaces
)

@Composable
fun GameDBTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme, // Appliquer le thème sombre
        typography = Typography,
        content = content
    )
}