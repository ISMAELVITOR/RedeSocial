package com.example.redesocial.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

object FirebaseConfig {

    val auth = FirebaseAuth.getInstance()

    val firestore = FirebaseFirestore.getInstance()

    val storage = FirebaseStorage.getInstance()
}