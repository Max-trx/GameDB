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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gamedatabase.data.UserRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onSignUp: () -> Unit,
    userRepository: UserRepository,
    modifier: Modifier = Modifier,
    rawgViewModel: CombinedViewModel
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope() // Créer un scope pour les coroutines

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Login", style = MaterialTheme.typography.headlineLarge)

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
                val users = userRepository.getAllUsers().firstOrNull() ?: emptyList()
                val user = users.find { it.userName == username && it.userPassword == password }
                if (user != null) {
                    Log.d("LoginScreen", "Logged in User ID: ${user.id}")
                    rawgViewModel.setLoggedInUser(user.id)
                    onLoginSuccess()
                } else {
                    loginError = true
                }
            }
        }) {
            Text("Login")
        }


        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onSignUp) {
            Text("Sign Up")
        }

        if (loginError) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Invalid credentials. Please try again.",
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
