package com.example.redesocial.screens

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.redesocial.firebase.FirebaseConfig
import com.example.redesocial.model.User
import com.example.redesocial.ui.theme.Blue700
import com.example.redesocial.ui.theme.Cyan500
import com.example.redesocial.ui.theme.Indigo950
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(
    navController: NavController,
    user: User?
) {
    val authUser = FirebaseAuth.getInstance().currentUser

    var currentUser by remember { mutableStateOf(user) }
    var isLoading by remember { mutableStateOf(true) }
    var isEditing by remember { mutableStateOf(false) }
    var editedName by remember { mutableStateOf("") }
    var editedBirthDate by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(authUser?.uid) {
        val uid = authUser?.uid

        if (uid.isNullOrBlank()) {
            currentUser = null
            isLoading = false
            return@LaunchedEffect
        }

        FirebaseConfig.firestore
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { snapshot ->
                currentUser = snapshot.toObject(User::class.java) ?: User(
                    uid = uid,
                    name = authUser.displayName ?: "",
                    email = authUser.email ?: ""
                )
                editedName = currentUser?.name.orEmpty()
                editedBirthDate = currentUser?.birthDate.orEmpty()
                isLoading = false
            }
            .addOnFailureListener {
                currentUser = User(
                    uid = uid,
                    name = authUser.displayName ?: "",
                    email = authUser.email ?: ""
                )
                editedName = currentUser?.name.orEmpty()
                editedBirthDate = currentUser?.birthDate.orEmpty()
                isLoading = false
            }
    }

    val displayName = currentUser?.name?.takeIf { it.isNotBlank() }
        ?: authUser?.displayName?.takeIf { it.isNotBlank() }
        ?: "Nome nao encontrado"
    val displayEmail = currentUser?.email?.takeIf { it.isNotBlank() }
        ?: authUser?.email?.takeIf { it.isNotBlank() }
        ?: "Email nao encontrado"
    val displayBirthDate = currentUser?.birthDate?.takeIf { it.isNotBlank() }
        ?: "Nao informada"

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
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = displayName,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        IconButton(onClick = {
                            editedName = displayName
                            editedBirthDate = displayBirthDate.takeIf { it != "Nao informada" }.orEmpty()
                            isEditing = true
                            errorMessage = ""
                        }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar perfil"
                            )
                        }
                    }
                }

                if (errorMessage.isNotBlank()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (isEditing) {
                    OutlinedTextField(
                        value = editedName,
                        onValueChange = { editedName = it },
                        label = { Text("Nome") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = editedBirthDate,
                        onValueChange = { editedBirthDate = it },
                        label = { Text("Data de nascimento") },
                        placeholder = { Text("dd/mm/aaaa") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            val uid = authUser?.uid.orEmpty()
                            val safeName = editedName.trim()
                            val safeBirthDate = editedBirthDate.trim()

                            if (uid.isBlank() || safeName.isBlank()) {
                                errorMessage = "Preencha o nome."
                                return@Button
                            }

                            val updatedUser = (currentUser ?: User(uid = uid)).copy(
                                uid = uid,
                                name = safeName,
                                email = displayEmail,
                                birthDate = safeBirthDate
                            )

                            FirebaseConfig.firestore
                                .collection("users")
                                .document(uid)
                                .set(updatedUser)
                                .addOnSuccessListener {
                                    currentUser = updatedUser
                                    isEditing = false
                                    errorMessage = ""
                                }
                                .addOnFailureListener { exception ->
                                    errorMessage = exception.localizedMessage
                                        ?: "Nao foi possivel salvar."
                                }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Salvar")
                    }

                    OutlinedButton(
                        onClick = {
                            isEditing = false
                            errorMessage = ""
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cancelar")
                    }
                } else {
                    ProfileInfo(label = "Nome", value = displayName)
                    ProfileInfo(label = "Email", value = displayEmail)
                    ProfileInfo(label = "Data de nascimento", value = displayBirthDate)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Voltar")
                    }

                    Button(
                        onClick = {
                            FirebaseConfig.auth.signOut()
                            navController.navigate("login") {
                                popUpTo("feed") { inclusive = true }
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Sair")
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileInfo(
    label: String,
    value: String
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
