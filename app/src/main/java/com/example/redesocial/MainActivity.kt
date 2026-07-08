package com.example.redesocial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.redesocial.navigation.AppRoot
import com.example.redesocial.ui.theme.RedeSocialTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RedeSocialTheme {
                AppRoot()
            }
        }
    }
}
