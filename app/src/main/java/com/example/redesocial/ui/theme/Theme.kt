package com.example.redesocial.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val BlueColorScheme = darkColorScheme(
    primary = AccentBlue,
    onPrimary = White,
    secondary = LightBlueText,
    onSecondary = DeepBlueBg,
    background = DeepBlueBg,
    onBackground = White,
    surface = CardBlue,
    onSurface = White,
    surfaceVariant = FieldBlue,
    onSurfaceVariant = LightBlueText,
    primaryContainer = AccentBlue.copy(alpha = 0.2f),
    onPrimaryContainer = LightBlueText,
    error = ErrorRed
)

@Composable
fun RedeSocialTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = BlueColorScheme,
        typography = Typography,
        content = content
    )
}
