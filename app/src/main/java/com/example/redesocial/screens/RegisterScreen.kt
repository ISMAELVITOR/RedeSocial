package com.example.redesocial.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.redesocial.firebase.FirebaseConfig
import com.example.redesocial.model.User
import com.example.redesocial.ui.theme.Blue700
import com.example.redesocial.ui.theme.Cyan500
import com.example.redesocial.ui.theme.Indigo950

@Composable
fun RegisterScreen(navController: NavController) {

    var name by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var birthDate by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var loading by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf("")
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
                    text = "Criar conta",
                    style = MaterialTheme.typography.headlineMedium
                )

                Text(
                    text = "Cadastre-se para participar da rede.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = birthDate,
                    onValueChange = { birthDate = it },
                    label = { Text("Data de nascimento") },
                    placeholder = { Text("dd/mm/aaaa") },
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
                    enabled = !loading,
                    onClick = {
                        val safeName = name.trim()
                        val safeEmail = email.trim()
                        val safeBirthDate = birthDate.trim()
                        val safePassword = password.trim()

                        if (safeName.isBlank() || safeEmail.isBlank() || safeBirthDate.isBlank() || safePassword.isBlank()) {
                            errorMessage = "Preencha nome, email, data de nascimento e senha."
                            return@Button
                        }

                        loading = true
                        errorMessage = ""

                        FirebaseConfig.auth
                            .createUserWithEmailAndPassword(safeEmail, safePassword)
                            .addOnSuccessListener {
                                val uid = FirebaseConfig.auth.currentUser?.uid.orEmpty()

                                if (uid.isBlank()) {
                                    loading = false
                                    errorMessage = "Nao foi possivel identificar o usuario criado."
                                    return@addOnSuccessListener
                                }

                                val user = User(
                                    uid = uid,
                                    name = safeName,
                                    email = safeEmail,
                                    birthDate = safeBirthDate
                                )

                                FirebaseConfig.firestore
                                    .collection("users")
                                    .document(uid)
                                    .set(user)
                                    .addOnSuccessListener {
                                        loading = false
                                        navController.navigate("feed") {
                                            popUpTo("login") {
                                                inclusive = true
                                            }
                                        }
                                    }
                                    .addOnFailureListener { exception ->
                                        loading = false
                                        errorMessage = exception.localizedMessage
                                            ?: "Erro ao salvar o perfil."
                                    }
                            }
                            .addOnFailureListener { exception ->
                                loading = false
                                errorMessage = exception.localizedMessage ?: "Erro ao cadastrar."
                            }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = if (loading) "Cadastrando..." else "Cadastrar")
                }

                OutlinedButton(
                    onClick = {
                        navController.navigate("login")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Já tenho conta")
                }
            }
        }
    }
}
