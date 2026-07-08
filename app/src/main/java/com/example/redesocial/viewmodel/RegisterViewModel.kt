package com.example.redesocial.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.redesocial.firebase.FirebaseConfig
import com.example.redesocial.model.User
import com.example.redesocial.utils.onlyDigits

class RegisterViewModel : ViewModel() {
    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var birthDate by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    fun onRegisterClick(onSuccess: () -> Unit) {
        isLoading = true
        FirebaseConfig.auth.createUserWithEmailAndPassword(email.trim(), password.trim())
            .addOnSuccessListener { auth ->
                val user = User(auth.user?.uid ?: "", name, email, birthDate.onlyDigits().take(8))
                FirebaseConfig.firestore.collection("users").document(user.uid).set(user)
                    .addOnSuccessListener { 
                        isLoading = false
                        onSuccess() 
                    }
            }
            .addOnFailureListener { 
                isLoading = false
                errorMessage = "Erro no cadastro"
            }
    }
}
