package com.example.redesocial.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Cyan500,
    onPrimary = Indigo950,
    primaryContainer = Indigo900,
    onPrimaryContainer = Slate50,
    secondary = Emerald500,
    onSecondary = Indigo950,
    tertiary = Amber500,
    onTertiary = Indigo950,
    background = Indigo950,
    onBackground = Slate50,
    surface = Slate800,
    onSurface = Slate50,
    surfaceVariant = Slate700,
    onSurfaceVariant = Slate100
)

private val LightColorScheme = lightColorScheme(
    primary = Blue700,
    onPrimary = Slate50,
    primaryContainer = Slate100,
    onPrimaryContainer = Indigo950,
    secondary = Cyan500,
    onSecondary = Indigo950,
    tertiary = Rose500,
    onTertiary = Slate50,
    background = Slate50,
    onBackground = Indigo950,
    surface = Slate50,
    onSurface = Indigo950,
    surfaceVariant = Slate100,
    onSurfaceVariant = Slate700
)

@Composable
fun RedeSocialTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
