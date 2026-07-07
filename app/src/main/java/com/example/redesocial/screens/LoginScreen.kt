package com.example.redesocial.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.redesocial.firebase.FirebaseConfig
import com.example.redesocial.ui.theme.Blue700
import com.example.redesocial.ui.theme.Cyan500
import com.example.redesocial.ui.theme.Indigo950

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Indigo950,
                        Blue700,
                        Cyan500
                    )
                )
            )
            .padding(20.dp)
    ) {
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "RedeSocial",
                    style = MaterialTheme.typography.headlineMedium
                )

                Text(
                    text = "Entre com seu email e senha para acessar seu feed.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Senha") },
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorMessage.isNotBlank()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Button(
                    enabled = !isLoading,
                    onClick = {
                        val safeEmail = email.trim()
                        val safePassword = password.trim()

                        if (safeEmail.isBlank() || safePassword.isBlank()) {
                            errorMessage = "Preencha email e senha."
                            return@Button
                        }

                        isLoading = true
                        errorMessage = ""

                        FirebaseConfig.auth
                            .signInWithEmailAndPassword(safeEmail, safePassword)
                            .addOnSuccessListener {
                                isLoading = false
                                navController.navigate("feed") {
                                    popUpTo("login") {
                                        inclusive = true
                                    }
                                }
                            }
                            .addOnFailureListener { exception ->
                                isLoading = false
                                errorMessage = exception.localizedMessage
                                    ?: "Nao foi possivel entrar."
                            }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = if (isLoading) "Entrando..." else "Entrar")
                }

                OutlinedButton(
                    onClick = {
                        navController.navigate("register")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Criar Conta")
                }
            }
        }
    }
}
