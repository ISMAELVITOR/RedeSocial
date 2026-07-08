package com.example.redesocial.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.redesocial.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel = viewModel()) {
    val user = viewModel.userProfile
    var isEditing by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Seu Perfil", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(32.dp))

        // Foto fake/Placeholder circular
        Surface(Modifier.size(100.dp).clip(CircleShape), color = MaterialTheme.colorScheme.secondaryContainer) {
            Box(contentAlignment = Alignment.Center) {
                Text(user?.name?.take(1)?.uppercase() ?: "?", style = MaterialTheme.typography.displaySmall)
            }
        }

        Spacer(Modifier.height(24.dp))

        if (isEditing) {
            var name by remember { mutableStateOf(user?.name ?: "") }
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nome") })
            Spacer(Modifier.height(16.dp))
            Button(onClick = { viewModel.updateProfile(name); isEditing = false }) { Text("Salvar") }
        } else {
            Text(user?.name ?: "Carregando...", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(user?.email ?: "", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.outline)
            
            Spacer(Modifier.height(32.dp))
            
            OutlinedButton(onClick = { isEditing = true }) { Text("Editar Nome") }
        }
    }
}
