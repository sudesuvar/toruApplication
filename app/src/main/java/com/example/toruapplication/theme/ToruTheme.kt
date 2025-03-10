package com.example.toruapplication.theme


import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFA29DE0), // Primary
    onPrimary = Color(0xFFFFFFFF), // White
    secondary = Color(0xFF605B85), // Secondary
    tertiary = Color(0xFFE3D7ED), // Tertiary
    background = Color(0xFF121212), // Dark background (genellikle koyu temalarda)
    surface = Color(0xFF1E1E1E), // Dark yüzey rengi
    onBackground = Color(0xFFDCDCDC), // BrokenWhite
    //onSurface = Color(0xFFDCDCDC) // BrokenWhite         // Yüzey rengi üzerine yazı rengi
    onSurface = Color.Red
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFA29DE0), // Primary
    secondary = Color(0xFF605B85), // Secondary
    tertiary = Color(0xFFE3D7ED),
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black
)


@Composable
fun ToruTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    Log.i( "TORUTHEME","Recomposition happened! isDarkTheme: $isDarkTheme")
    val colors = if (isDarkTheme) DarkColorScheme else LightColorScheme

        MaterialTheme(
            colorScheme = colors,
            content = content
        )

}

