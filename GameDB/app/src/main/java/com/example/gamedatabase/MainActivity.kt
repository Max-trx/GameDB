package com.example.gamedatabase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.gamedatabase.ui.RawgApp
import com.example.gamedatabase.ui.theme.GameDBTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            GameDBTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    RawgApp()
                }
            }
        }
    }
}