package com.example.gamedb.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamedb.R

@Composable
fun Description(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Top Navigation Arrow
        Image(
            painter = painterResource(id = R.drawable.arrow),
            contentDescription = "Back Arrow",
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .size(50.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(120.dp)) // Top margin for the title

            // Title
            Text(
                text = "Red Dead Redemption II",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Game Image
            Image(
                painter = painterResource(id = R.drawable.rdr2),
                contentDescription = "Game Thumbnail",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(30.dp))
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Description Section
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)) {
                        append("Description: ")
                    }
                    withStyle(style = SpanStyle(color = Color.White, fontSize = 16.sp)) {
                        append(
                            "After a robbery goes badly wrong in the western town of Blackwater, Arthur Morgan " +
                                    "and the Van der Linde gang are forced to flee. With federal agents and the best " +
                                    "bounty hunters in the nation massing on their heels, the gang must rob, steal " +
                                    "and fight their way across the rugged heartland of America in order to survive."
                        )
                    }
                },
                style = TextStyle(lineHeight = 22.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            // Release Date Section
            InfoRow(label = "Release Date:", value = "26 October 2018")

            // Developers Section
            InfoRow(label = "Developers:", value = "Rockstar Games")

            // Platforms Section
            InfoRow(
                label = "Platforms:",
                value = "PlayStation 4, Xbox One, Google Stadia, Microsoft Windows"
            )
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)) {
                append(label)
            }
            withStyle(style = SpanStyle(color = Color.White, fontSize = 16.sp)) {
                append(" $value")
            }
        },
        style = TextStyle(lineHeight = 22.sp),
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun DescriptionPreview() {
    Description()
}
