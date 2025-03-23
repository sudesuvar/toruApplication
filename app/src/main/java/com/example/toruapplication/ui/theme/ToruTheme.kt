package com.example.toruapplication.ui.theme


import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import android.app.Activity
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat

val Purple80 = Color(0xFFB45E95) // Eski: D0BCFF -> Yeni: B45E95
val PurpleGrey80 = Color(0xFFCCC2DC) // Bu rengi değiştirmedim, istersen değiştirebilirsin.
val Pink80 = Color(0xFFEFB8C8) // Bu rengi değiştirmedim, istersen değiştirebilirsin.

val Purple40 = Color(0xFFB45E95) // Eski: 6650a4 -> Yeni: B45E95
val PurpleGrey40 = Color(0xFF625B71) // Bu rengi değiştirmedim, istersen değiştirebilirsin.
val Pink40 = Color(0xFF7D5260) // Bu rengi değiştirmedim, istersen değiştirebilirsin.


private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Color(0xFF121212), // Koyu arka plan rengi
    surface = Color(0xFF1E1E1E),   // Koyu yüzey rengi
    onPrimary = Color.White,        // Ana rengi üzerine yazı rengi
    onSecondary = Color.White,      // İkincil rengi üzerine yazı rengi
    onTertiary = Color.White,       // Üçüncül rengi üzerine yazı rengi
    onBackground = Color.White,     // Arka plan rengi üzerine yazı rengi
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = Color(0xFFFFFBFE), // Açık arka plan rengi
    surface = Color(0xFFFFFFFF),     // Açık yüzey rengi
    onPrimary = Color.Black,         // Ana rengi üzerine yazı rengi
    onSecondary = Color.Black,       // İkincil rengi üzerine yazı rengi
    onTertiary = Color.Black,        // Üçüncül rengi üzerine yazı rengi
    onBackground = Color.Black,      // Arka plan rengi üzerine yazı rengi
    onSurface = Color.White
)


@Composable
fun ToruTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
