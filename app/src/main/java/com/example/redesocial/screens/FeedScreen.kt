package com.example.redesocial.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.redesocial.utils.ImageUtils
import com.example.redesocial.viewmodel.FeedViewModel

@Composable
fun FeedScreen(navController: NavController, viewModel: FeedViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            shadowElevation = 8.dp
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Feed",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 1.sp
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(viewModel.posts) { post ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val authorBitmap = ImageUtils.base64ToBitmapOrNull(post.authorProfileImageBase64)

                            if (authorBitmap != null) {
                                Image(
                                    bitmap = authorBitmap.asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Surface(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape),
                                    color = MaterialTheme.colorScheme.primaryContainer
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = post.authorName.take(1).uppercase(),
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }

                            Spacer(Modifier.width(12.dp))

                            Text(
                                text = post.authorName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        Text(post.text)

                        if (post.imageBase64.isNotEmpty()) {
                            Spacer(Modifier.height(12.dp))
                            ImageUtils.base64ToBitmapOrNull(post.imageBase64)?.let {
                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
