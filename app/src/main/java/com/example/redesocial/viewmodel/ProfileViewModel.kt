package com.example.redesocial.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.redesocial.firebase.FirebaseConfig
import com.example.redesocial.model.User
import com.google.firebase.auth.userProfileChangeRequest

class ProfileViewModel : ViewModel() {
    var userProfile by mutableStateOf<User?>(null)
        private set

    init {
        loadProfile()
    }

    private fun loadProfile() {
        val uid = FirebaseConfig.auth.currentUser?.uid ?: return
        FirebaseConfig.firestore.collection("users").document(uid).get()
            .addOnSuccessListener { snapshot ->
                userProfile = snapshot.toObject(User::class.java)
                    ?: User(
                        uid = uid,
                        name = FirebaseConfig.auth.currentUser?.displayName ?: "",
                        email = FirebaseConfig.auth.currentUser?.email ?: ""
                    )
            }
            .addOnFailureListener {
                userProfile = User(
                    uid = uid,
                    name = FirebaseConfig.auth.currentUser?.displayName ?: "",
                    email = FirebaseConfig.auth.currentUser?.email ?: ""
                )
            }
    }

    fun updateProfile(newName: String, newBirthDate: String, newProfileImageBase64: String) {
        val user = FirebaseConfig.auth.currentUser ?: return
        val updatedProfile = (userProfile ?: User(uid = user.uid, email = user.email ?: "")).copy(
            uid = user.uid,
            name = newName,
            email = user.email ?: "",
            birthDate = newBirthDate,
            profileImageBase64 = newProfileImageBase64
        )

        val request = userProfileChangeRequest {
            displayName = newName
        }

        user.updateProfile(request).addOnSuccessListener {
            FirebaseConfig.firestore.collection("users").document(user.uid)
                .set(updatedProfile)
                .addOnSuccessListener {
                    userProfile = updatedProfile
                }
        }
    }
}
