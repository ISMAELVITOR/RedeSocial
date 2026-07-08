package com.example.redesocial.model

data class Post(
    val authorUid: String = "",
    val authorName: String = "",
    val authorProfileImageBase64: String = "",
    val text: String = "",
    val imageBase64: String = "",
    val createdAt: Long = 0L
)
