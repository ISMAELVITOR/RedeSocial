package com.example.redesocial.screens

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.redesocial.firebase.FirebaseConfig
import com.example.redesocial.model.Post
import com.example.redesocial.ui.theme.Blue700
import com.example.redesocial.ui.theme.Cyan500
import com.example.redesocial.ui.theme.Indigo950
import com.example.redesocial.utils.ImageUtils
import com.google.firebase.auth.FirebaseAuth

@Composable
fun CreatePostScreen(navController: NavController) {
    val context = LocalContext.current

    var postText by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageBase64 by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri = uri

        if (uri != null) {
            val bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            }

            imageBase64 = ImageUtils.bitmapToBase64(ImageUtils.resizeBitmap(bitmap))
        }
    }

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
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Nova publicacao",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Escreva sua publicacao, escolha uma imagem se quiser e publique.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (errorMessage.isNotBlank()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                OutlinedTextField(
                    value = postText,
                    onValueChange = { postText = it },
                    label = { Text("Escreva sua publicacao") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4
                )

                Button(
                    onClick = { launcher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Adicionar imagem")
                }

                if (imageUri != null) {
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val bitmap = if (Build.VERSION.SDK_INT < 28) {
                            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
                        } else {
                            val source = ImageDecoder.createSource(context.contentResolver, imageUri!!)
                            ImageDecoder.decodeBitmap(source)
                        }

                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        enabled = !isSaving,
                        onClick = {
                            val safeText = postText.trim()
                            if (safeText.isBlank() && imageBase64.isBlank()) {
                                errorMessage = "Escreva um texto ou selecione uma imagem."
                                return@Button
                            }

                            isSaving = true
                            errorMessage = ""

                            val user = FirebaseAuth.getInstance().currentUser

                            val post = Post(
                                authorUid = user?.uid.orEmpty(),
                                userName = user?.email ?: "Usuario",
                                text = safeText,
                                imageBase64 = imageBase64
                            )

                            FirebaseConfig.firestore
                                .collection("posts")
                                .add(post)
                                .addOnSuccessListener {
                                    isSaving = false
                                    postText = ""
                                    imageBase64 = ""
                                    imageUri = null
                                    navController.navigate("feed") {
                                        popUpTo("feed") { inclusive = true }
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    isSaving = false
                                    errorMessage = exception.localizedMessage
                                        ?: "Nao foi possivel publicar."
                                }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (isSaving) "Publicando..." else "Publicar")
                    }

                    Button(
                        onClick = {
                            postText = ""
                            imageBase64 = ""
                            imageUri = null
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Limpar")
                    }
                }
            }
        }
    }
}
