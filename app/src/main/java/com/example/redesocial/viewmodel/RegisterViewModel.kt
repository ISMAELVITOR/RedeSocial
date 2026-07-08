package com.example.redesocial.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.redesocial.firebase.FirebaseConfig
import com.example.redesocial.model.User

class RegisterViewModel : ViewModel() {
    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var birthDate by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false) // Unificado com LoginViewModel
    var errorMessage by mutableStateOf("")

    fun onRegisterClick(onSuccess: () -> Unit) {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            errorMessage = "Preencha todos os campos obrigatórios."
            return
        }

        isLoading = true
        errorMessage = ""

        FirebaseConfig.auth.createUserWithEmailAndPassword(email.trim(), password.trim())
            .addOnSuccessListener { authResult ->
                val uid = authResult.user?.uid ?: ""
                val user = User(uid, name.trim(), email.trim(), birthDate.trim())

                FirebaseConfig.firestore.collection("users").document(uid).set(user)
                    .addOnSuccessListener {
                        isLoading = false
                        onSuccess()
                    }
                    .addOnFailureListener {
                        isLoading = false
                        errorMessage = "Erro ao salvar dados no banco."
                    }
            }
            .addOnFailureListener {
                isLoading = false
                errorMessage = it.localizedMessage ?: "Erro ao cadastrar."
            }
    }
}
