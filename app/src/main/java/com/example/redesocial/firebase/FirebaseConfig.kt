package com.example.redesocial.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseConfig {
    val auth: FirebaseAuth get() = FirebaseAuth.getInstance()
    val firestore: FirebaseFirestore get() = FirebaseFirestore.getInstance()
}
