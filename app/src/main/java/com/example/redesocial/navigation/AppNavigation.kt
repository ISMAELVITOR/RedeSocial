package com.example.redesocial.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.redesocial.firebase.FirebaseConfig
import com.example.redesocial.screens.*

@Composable
fun AppNavigation(navController: NavHostController, startDestination: String) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute in setOf("feed", "createPost", "profile")

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute == "feed",
                        onClick = { navController.navigate("feed") { launchSingleTop = true } },
                        icon = { Icon(Icons.Default.Home, "Feed") },
                        label = { Text("Feed") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == "createPost",
                        onClick = { navController.navigate("createPost") { launchSingleTop = true } },
                        icon = { Icon(Icons.Default.Add, "Novo") },
                        label = { Text("Postar") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == "profile",
                        onClick = { navController.navigate("profile") { launchSingleTop = true } },
                        icon = { Icon(Icons.Default.Person, "Perfil") },
                        label = { Text("Perfil") }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = {
                            FirebaseConfig.auth.signOut()
                            navController.navigate("login") { popUpTo(0) }
                        },
                        icon = { Icon(Icons.Default.ExitToApp, "Sair") },
                        label = { Text("Sair") }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination, Modifier.padding(innerPadding)) {
            composable("login") { LoginScreen(navController) }
            composable("register") { RegisterScreen(navController) }
            composable("feed") { FeedScreen(navController) }
            composable("createPost") { CreatePostScreen(navController) }
            composable("profile") { ProfileScreen(navController) }
        }
    }
}
