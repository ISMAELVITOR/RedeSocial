package com.example.redesocial.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.redesocial.utils.ImageUtils
import com.example.redesocial.viewmodel.FeedViewModel

@Composable
fun FeedScreen(navController: NavController, viewModel: FeedViewModel = viewModel()) {
    Scaffold(
        topBar = {
            Surface(shadowElevation = 4.dp) {
                Text("Feed", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(16.dp).fillMaxWidth())
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(viewModel.posts) { post ->
                Card(modifier = Modifier.padding(8.dp).fillMaxWidth()) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(post.authorName, style = MaterialTheme.typography.titleMedium)
                        Text(post.text)
                        if (post.imageBase64.isNotEmpty()) {
                            ImageUtils.base64ToBitmapOrNull(post.imageBase64)?.let {
                                Image(bitmap = it.asImageBitmap(), contentDescription = null, modifier = Modifier.height(200.dp).fillMaxWidth())
                            }
                        }
                    }
                }
            }
        }
    }
}
