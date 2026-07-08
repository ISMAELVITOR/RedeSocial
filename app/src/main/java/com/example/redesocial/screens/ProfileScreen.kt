package com.example.redesocial.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.redesocial.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel = viewModel()) {
    val user = viewModel.userProfile
    var name by remember { mutableStateOf("") }

    Column(Modifier.padding(24.dp)) {
        Text("Perfil", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        if (user != null) {
            Text("Email: ${user.email}")
            Spacer(Modifier.height(16.dp))
            
            OutlinedTextField(
                value = if (name.isEmpty() && name.isBlank()) user.name else name,
                onValueChange = { name = it },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(Modifier.height(8.dp))
            
            Button(onClick = { viewModel.updateProfile(name) }, modifier = Modifier.fillMaxWidth()) {
                Text("Atualizar Nome")
            }
        }
    }
}
