package com.example.redesocial.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.example.redesocial.firebase.FirebaseConfig
import com.example.redesocial.model.Post
import com.example.redesocial.utils.ImageUtils

class CreatePostViewModel : ViewModel() {

    fun createPost(text: String, bitmap: Bitmap?, onSuccess: () -> Unit = {}) {
        val auth = FirebaseConfig.auth.currentUser ?: return
        val imageBase64 = bitmap?.let { ImageUtils.bitmapToBase64(it) } ?: ""

        val newPost = Post(
            authorUid = auth.uid,
            userName = auth.displayName ?: "Usuário",
            text = text,
            imageBase64 = imageBase64,
            createdAt = System.currentTimeMillis()
        )

        FirebaseConfig.firestore.collection("posts")
            .add(newPost) // Usando add() para o Firestore gerar o ID automaticamente
            .addOnSuccessListener { onSuccess() }
    }
}
