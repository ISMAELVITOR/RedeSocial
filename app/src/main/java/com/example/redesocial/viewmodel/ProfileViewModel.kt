package com.example.redesocial.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.redesocial.firebase.FirebaseConfig
import com.example.redesocial.model.User

class ProfileViewModel : ViewModel() {
    var userProfile by mutableStateOf<User?>(null)
        private set

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        val uid = FirebaseConfig.auth.currentUser?.uid ?: return
        FirebaseConfig.firestore.collection("users").document(uid).get()
            .addOnSuccessListener { userProfile = it.toObject(User::class.java) }
    }

    fun updateProfile(newName: String) {
        val uid = FirebaseConfig.auth.currentUser?.uid ?: return
        FirebaseConfig.firestore.collection("users").document(uid)
            .update("name", newName)
            .addOnSuccessListener {
                userProfile = userProfile?.copy(name = newName)
            }
    }
}
