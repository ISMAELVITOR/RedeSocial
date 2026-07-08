package com.example.redesocial.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.redesocial.firebase.FirebaseConfig
import com.example.redesocial.model.Post
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query

class FeedViewModel : ViewModel() {
    var posts by mutableStateOf(listOf<Post>())
        private set
    var isInitialLoading by mutableStateOf(true)
        private set
    var isLoadingMore by mutableStateOf(false)
        private set
    var hasMorePosts by mutableStateOf(true)
        private set
    var errorMessage by mutableStateOf("")
        private set

    private val pageSize = 20
    private var lastVisiblePost: DocumentSnapshot? = null
    private var didLoadInitial = false

    init {
        loadInitialPosts()
    }

    fun loadInitialPosts() {
        if (didLoadInitial) return
        didLoadInitial = true
        isInitialLoading = true
        errorMessage = ""
        hasMorePosts = true
        lastVisiblePost = null
        posts = emptyList()

        FirebaseConfig.firestore.collection("posts")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(pageSize.toLong())
            .get()
            .addOnSuccessListener { snapshot ->
                val loadedPosts = snapshot.documents.mapNotNull { it.toObject(Post::class.java) }
                posts = loadedPosts
                lastVisiblePost = snapshot.documents.lastOrNull()
                hasMorePosts = snapshot.size() == pageSize
                isInitialLoading = false
            }
            .addOnFailureListener { exception ->
                errorMessage = exception.message ?: "Erro ao carregar feed"
                isInitialLoading = false
            }
    }

    fun loadMorePosts() {
        if (isInitialLoading || isLoadingMore || !hasMorePosts) return
        val lastVisible = lastVisiblePost ?: return

        isLoadingMore = true
        errorMessage = ""

        FirebaseConfig.firestore.collection("posts")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .startAfter(lastVisible)
            .limit(pageSize.toLong())
            .get()
            .addOnSuccessListener { snapshot ->
                val morePosts = snapshot.documents.mapNotNull { it.toObject(Post::class.java) }
                posts = posts + morePosts
                lastVisiblePost = snapshot.documents.lastOrNull()
                hasMorePosts = snapshot.size() == pageSize
                isLoadingMore = false
            }
            .addOnFailureListener { exception ->
                errorMessage = exception.message ?: "Erro ao carregar mais posts"
                isLoadingMore = false
            }
    }
}
