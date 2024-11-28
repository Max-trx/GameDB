package com.example.gamedb

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamedb.ui.theme.GameDBTheme


class MainPage : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GameDBTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    Acceuil()
                }
            }
        }
    }
}

@Composable
fun Acceuil() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Top Bar
            Image(
                painter = painterResource(id = R.drawable.burger),
                contentDescription = "Menu Icon",
                modifier = Modifier
                    .align(Alignment.Start)
                    .size(50.dp)
                    .padding(8.dp),
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar
            HomeSearch()

            Spacer(modifier = Modifier.height(24.dp))

            // Section Title
            Text(
                text = "Popular Games",
                style = TextStyle(
                    fontSize = 24.sp,
                    color = Color.White,
                    textAlign = TextAlign.Start
                ),
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Game Grid (Two per row)
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                items(getGames().chunked(2)) { gamePair ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        gamePair.forEach { game ->
                            GameCard(
                                title = game.title,
                                imageRes = game.imageRes,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // Add spacer if only one item in this row
                        if (gamePair.size < 2) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

// A data class for game information
data class Game(val title: String, val imageRes: Int)

// A function to provide the list of games
fun getGames(): List<Game> = listOf(
    Game("Red Dead Redemption II", R.drawable.rdr2),
    Game("Apex Legends", R.drawable.apex),
    Game("Minecraft", R.drawable.minecraft),
    Game("Grand Theft Auto V", R.drawable.grand_theft_auto_v)
)

@Composable
fun GameCard(title: String, imageRes: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(200.dp)
            .clip(shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp))
            .background(Color.Gray)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        )
        Text(
            text = title,
            style = TextStyle(
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(8.dp)
        )
    }
}

@Composable
fun HomeSearch() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
            .background(Color.White)
            .border(
                width = 1.dp,
                color = Color.Black.copy(alpha = 0.1f),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.search),
            contentDescription = "Search Icon",
            tint = Color.Black.copy(alpha = 0.7f),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Search games",
            style = TextStyle(
                color = Color.Black.copy(alpha = 0.74f),
                fontSize = 16.sp
            ),
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
fun AcceuilPreview() {
    GameDBTheme {
        Acceuil()
    }
}
