package com.example.reciepe_native_app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFFB54714),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDBCB),
    onPrimaryContainer = Color(0xFF3A0C00),
    secondary = Color(0xFF2F6A4A),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFB3F1CC),
    onSecondaryContainer = Color(0xFF002111),
    tertiary = Color(0xFF6B5C2F),
    background = Color(0xFFFFF8F5),
    surface = Color(0xFFFFF8F5),
    surfaceVariant = Color(0xFFF4DED5),
    error = Color(0xFFBA1A1A),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFFFB694),
    onPrimary = Color(0xFF5E1700),
    primaryContainer = Color(0xFF852200),
    onPrimaryContainer = Color(0xFFFFDBCB),
    secondary = Color(0xFF97D4B1),
    onSecondary = Color(0xFF003823),
    secondaryContainer = Color(0xFF145234),
    onSecondaryContainer = Color(0xFFB3F1CC),
    tertiary = Color(0xFFD8C68A),
    background = Color(0xFF1A110E),
    surface = Color(0xFF1A110E),
    surfaceVariant = Color(0xFF52443D),
    error = Color(0xFFFFB4AB),
)

@Composable
fun RecipeBoxTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content,
    )
}
