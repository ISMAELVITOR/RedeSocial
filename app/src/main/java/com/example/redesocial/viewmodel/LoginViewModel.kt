package com.example.redesocial.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.redesocial.firebase.FirebaseConfig

class LoginViewModel : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    fun onLoginClick(onSuccess: () -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Preencha tudo"
            return
        }
        isLoading = true
        FirebaseConfig.auth.signInWithEmailAndPassword(email.trim(), password.trim())
            .addOnSuccessListener {
                isLoading = false
                onSuccess()
            }
            .addOnFailureListener {
                isLoading = false
                errorMessage = "Erro ao entrar"
            }
    }
}
