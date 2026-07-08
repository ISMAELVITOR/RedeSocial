package com.example.redesocial.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.redesocial.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(navController: NavController, viewModel: RegisterViewModel = viewModel()) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Criar Conta", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(value = viewModel.name, onValueChange = { viewModel.name = it }, label = { Text("Nome") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = viewModel.email, onValueChange = { viewModel.email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = viewModel.password, onValueChange = { viewModel.password = it }, label = { Text("Senha") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())

        if (viewModel.errorMessage.isNotBlank()) {
            Text(viewModel.errorMessage, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { viewModel.onRegisterClick { navController.navigate("feed") { popUpTo("login") { inclusive = true } } } },
            modifier = Modifier.fillMaxWidth(),
            enabled = !viewModel.isLoading
        ) {
            Text(if (viewModel.isLoading) "Carregando..." else "Cadastrar")
        }

        TextButton(onClick = { navController.popBackStack() }) {
            Text("Já tem conta? Entrar")
        }
    }
}
