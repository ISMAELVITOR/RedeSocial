package com.example.redesocial.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.redesocial.firebase.FirebaseConfig

@Composable
fun AppRoot() {
    val navController = rememberNavController()
    val authUser = FirebaseConfig.auth.currentUser

    AppNavigation(
        navController = navController,
        startDestination = if (authUser != null) "feed" else "login"
    )
}
