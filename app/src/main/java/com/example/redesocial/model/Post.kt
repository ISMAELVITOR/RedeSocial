package com.example.redesocial.model

data class Post(
    val authorUid: String = "",
    val userName: String = "",
    val text: String = "",
    val imageBase64: String = "",
    val createdAt: Long = 0L
)
