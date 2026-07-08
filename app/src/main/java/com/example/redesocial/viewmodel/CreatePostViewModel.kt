package com.example.redesocial.viewmodel

import androidx.lifecycle.ViewModel
import com.example.redesocial.firebase.FirebaseConfig
import com.example.redesocial.model.Post
import com.example.redesocial.model.User

class CreatePostViewModel : ViewModel() {
    fun createPost(text: String, imageBase64: String, onSuccess: () -> Unit) {
        val authUser = FirebaseConfig.auth.currentUser ?: return
        val uid = authUser.uid

        FirebaseConfig.firestore.collection("users").document(uid).get()
            .addOnSuccessListener { snapshot ->
                val profile = snapshot.toObject(User::class.java)

                val post = Post(
                    authorUid = uid,
                    authorName = profile?.name?.takeIf { it.isNotBlank() }
                        ?: authUser.displayName?.takeIf { it.isNotBlank() }
                        ?: "Usuario",
                    authorProfileImageBase64 = profile?.profileImageBase64.orEmpty(),
                    text = text,
                    imageBase64 = imageBase64,
                    createdAt = System.currentTimeMillis()
                )

                FirebaseConfig.firestore.collection("posts").add(post)
                    .addOnSuccessListener { onSuccess() }
            }
    }
}
