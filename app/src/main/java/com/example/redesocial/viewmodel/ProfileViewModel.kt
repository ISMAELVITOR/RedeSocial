package com.example.redesocial.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.redesocial.firebase.FirebaseConfig
import com.example.redesocial.model.User

class ProfileViewModel : ViewModel() {
    var userProfile by mutableStateOf<User?>(null)

    init {
        val uid = FirebaseConfig.auth.currentUser?.uid ?: ""
        FirebaseConfig.firestore.collection("users").document(uid).get()
            .addOnSuccessListener { userProfile = it.toObject(User::class.java) }
    }

    fun updateProfile(newName: String) {
        val uid = FirebaseConfig.auth.currentUser?.uid ?: return
        FirebaseConfig.firestore.collection("users").document(uid).update("name", newName)
            .addOnSuccessListener { userProfile = userProfile?.copy(name = newName) }
    }
}
