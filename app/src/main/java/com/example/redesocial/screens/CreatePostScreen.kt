package com.example.redesocial.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.redesocial.utils.ImageUtils
import com.example.redesocial.viewmodel.CreatePostViewModel

@Composable
fun CreatePostScreen(navController: NavController, viewModel: CreatePostViewModel = viewModel()) {
    var text by remember { mutableStateOf("") }
    var selectedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var imageBase64 by remember { mutableStateOf("") }
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult

        ImageUtils.loadBitmapFromUri(context, uri)?.let { bitmap ->
            val scaledBitmap = ImageUtils.resizeKeepingAspectRatio(bitmap, 1280)
            selectedBitmap = scaledBitmap

            imageBase64 = ImageUtils.bitmapToBase64Jpeg(scaledBitmap, 88)
        }
    }

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
            onClick = { launcher.launch("image/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (selectedBitmap == null) "Adicionar imagem" else "Trocar imagem")
        }

        if (selectedBitmap != null) {
            Spacer(Modifier.height(16.dp))
            Image(
                bitmap = selectedBitmap!!.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.createPost(text, imageBase64) {
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
