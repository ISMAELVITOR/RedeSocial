package com.example.redesocial.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.redesocial.firebase.FirebaseConfig

class LoginViewModel : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    fun onLoginClick(onSuccess: () -> Unit) {
        val safeEmail = email.trim()
        val safePassword = password.trim()

        if (safeEmail.isBlank() || safePassword.isBlank()) {
            errorMessage = "Preencha email e senha."
            return
        }

        isLoading = true
        errorMessage = ""

        FirebaseConfig.auth
            .signInWithEmailAndPassword(safeEmail, safePassword)
            .addOnSuccessListener {
                isLoading = false
                onSuccess()
            }
            .addOnFailureListener {
                isLoading = false
                errorMessage = it.localizedMessage ?: "Não foi possível entrar."
            }
    }
}
