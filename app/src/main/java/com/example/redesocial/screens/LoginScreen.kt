package com.example.redesocial.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.redesocial.viewmodel.LoginViewModel

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = viewModel()) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("LinkUp", style = MaterialTheme.typography.displayLarge)
        Spacer(Modifier.height(32.dp))

        OutlinedTextField(value = viewModel.email, onValueChange = { viewModel.email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = viewModel.password, onValueChange = { viewModel.password = it }, label = { Text("Senha") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())

        if (viewModel.errorMessage.isNotBlank()) {
            Text(viewModel.errorMessage, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { viewModel.onLoginClick { navController.navigate("feed") { popUpTo("login") { inclusive = true } } } },
            modifier = Modifier.fillMaxWidth(),
            enabled = !viewModel.isLoading
        ) {
            Text(if (viewModel.isLoading) "Entrando..." else "Entrar")
        }

        TextButton(onClick = { navController.navigate("register") }) {
            Text("Criar conta")
        }
    }
}
