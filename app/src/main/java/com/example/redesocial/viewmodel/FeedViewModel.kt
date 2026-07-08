package com.example.redesocial.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.redesocial.firebase.FirebaseConfig
import com.example.redesocial.model.Post
import com.google.firebase.firestore.ListenerRegistration

class FeedViewModel : ViewModel() {
    var posts by mutableStateOf(listOf<Post>())
        private set

    private var listenerRegistration: ListenerRegistration? = null

    init {
        observePosts()
    }

    private fun observePosts() {
        listenerRegistration = FirebaseConfig.firestore
            .collection("posts")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                if (value != null) {
                    posts = value.documents
                        .mapNotNull { it.toObject(Post::class.java) }
                        .sortedByDescending { it.createdAt }
                }
            }
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}
