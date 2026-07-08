package com.example.redesocial.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.redesocial.viewmodel.CreatePostViewModel

@Composable
fun CreatePostScreen(navController: NavController, viewModel: CreatePostViewModel = viewModel()) {
    var text by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(24.dp)) {
        Text("Nova Postagem", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("O que está pensando?") },
            modifier = Modifier.fillMaxWidth().height(150.dp)
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.createPost(text, "") {
                    navController.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = text.isNotBlank()
        ) {
            Text("Publicar")
        }
    }
}
