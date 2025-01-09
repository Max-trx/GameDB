package com.example.gamedatabase.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.gamedatabase.data.UserRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    onSignUpSuccess: () -> Unit,
    onSignIn: () -> Unit,
    userRepository: UserRepository,
    modifier: Modifier = Modifier,
    rawgViewModel: CombinedViewModel
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) } // État pour la boîte de dialogue

    val coroutineScope = rememberCoroutineScope() // Créer un scope pour les coroutines

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Sign Up", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") }
        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            coroutineScope.launch {
                try {
                    rawgViewModel.setNewUser(username, password, userRepository)
                    showDialog = true // Affiche la boîte de dialogue après succès
                } catch (e: Exception) {
                    Log.e("SignUp", "Error signing up: ${e.message}")
                    loginError = true
                }
            }
        }) {
            Text("Sign Up")
        }

        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onSignIn) {
            Text("Sign In")
        }

        if (loginError) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Invalid credentials. Please try again.",
                color = MaterialTheme.colorScheme.error
            )
        }
    }

    // Boîte de dialogue pour confirmer l'ajout de l'utilisateur
    if (showDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    onSignIn() // Navigue vers l'écran de connexion
                }) {
                    Text("OK")
                }
            },
            title = { Text("User Created") },
            text = { Text("Your account has been successfully created.") }
        )
    }
}

