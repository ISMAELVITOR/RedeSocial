package com.example.redesocial.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.example.redesocial.firebase.FirebaseConfig
import com.example.redesocial.model.User

@Composable
fun AppRoot() {
    val navController = rememberNavController()

    var currentUserProfile by remember {
        mutableStateOf<User?>(null)
    }

    fun loadCurrentUserProfile() {
        val authUser = FirebaseConfig.auth.currentUser

        if (authUser == null) {
            currentUserProfile = null
            return
        }

        FirebaseConfig.firestore
            .collection("users")
            .document(authUser.uid)
            .get()
            .addOnSuccessListener { snapshot ->
                currentUserProfile = snapshot.toObject(User::class.java) ?: User(
                    uid = authUser.uid,
                    name = authUser.displayName ?: "",
                    email = authUser.email ?: ""
                )
            }
            .addOnFailureListener {
                currentUserProfile = User(
                    uid = authUser.uid,
                    name = authUser.displayName ?: "",
                    email = authUser.email ?: ""
                )
            }
    }

    LaunchedEffect(Unit) {
        loadCurrentUserProfile()
    }

    AppNavigation(
        navController = navController,
        currentUserProfile = currentUserProfile,
        startDestination = if (FirebaseConfig.auth.currentUser != null) "feed" else "login"
    )
}
