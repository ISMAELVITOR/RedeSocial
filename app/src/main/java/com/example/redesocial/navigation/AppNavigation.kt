package com.example.redesocial.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavHostController
import com.example.redesocial.screens.CreatePostScreen
import com.example.redesocial.screens.FeedScreen
import com.example.redesocial.screens.LoginScreen
import com.example.redesocial.screens.ProfileScreen
import com.example.redesocial.screens.RegisterScreen
import com.example.redesocial.model.User

@Composable
fun AppNavigation(
    navController: NavHostController,
    currentUserProfile: User?,
    startDestination: String
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = currentRoute in setOf("feed", "createPost", "profile")

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute == "feed",
                        onClick = {
                            navController.navigate("feed") {
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Feed") },
                        label = { Text("Feed") }
                    )

                    NavigationBarItem(
                        selected = currentRoute == "profile",
                        onClick = {
                            navController.navigate("profile") {
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                        label = { Text("Perfil") }
                    )

                    NavigationBarItem(
                        selected = currentRoute == "createPost",
                        onClick = {
                            navController.navigate("createPost") {
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(Icons.Default.Add, contentDescription = "Novo") },
                        label = { Text("Novo") }
                    )

                    NavigationBarItem(
                        selected = false,
                        onClick = {
                            com.example.redesocial.firebase.FirebaseConfig.auth.signOut()
                            navController.navigate("login") {
                                popUpTo("feed") { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(Icons.Default.PowerSettingsNew, contentDescription = "Sair") },
                        label = { Text("Sair") }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable("login") {
                LoginScreen(navController)
            }

            composable("register") {
                RegisterScreen(navController)
            }

            composable("feed") {
                FeedScreen(navController)
            }

            composable("createPost") {
                CreatePostScreen(navController)
            }

            composable("profile") {
                ProfileScreen(navController, currentUserProfile)
            }
        }
    }
}
