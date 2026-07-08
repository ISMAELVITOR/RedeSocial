package com.example.redesocial.viewmodel

import androidx.lifecycle.ViewModel
import com.example.redesocial.firebase.FirebaseConfig
import com.example.redesocial.model.Post

class CreatePostViewModel : ViewModel() {
    fun createPost(text: String, imageBase64: String, onSuccess: () -> Unit) {
        val user = FirebaseConfig.auth.currentUser
        val post = Post(
            authorName = user?.displayName ?: "Usuário",
            text = text,
            imageBase64 = imageBase64,
            createdAt = System.currentTimeMillis()
        )
        FirebaseConfig.firestore.collection("posts").add(post).addOnSuccessListener { onSuccess() }
    }
}
