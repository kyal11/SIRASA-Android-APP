package com.dev.sirasa.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

val LightColorScheme = lightColorScheme(
    primary = Green700,
    onPrimary = White,
    primaryContainer = Green50,
    onPrimaryContainer = Green900,
    secondary = Green600,
    onSecondary = White,
    secondaryContainer = Green300,
    onSecondaryContainer = Green900,
    background = GrayBackground,
    onBackground = Black,
    surface = GrayBackground,
    surfaceVariant = White,
    error = Red,
    onError = White
)

val DarkColorScheme = darkColorScheme(
    primary = Green300,
    onPrimary = Black,
    primaryContainer = Green700,
    onPrimaryContainer = White,
    secondary = Green600,
    onSecondary = Black,
    secondaryContainer = Green800,
    onSecondaryContainer = White,
    background = Black,
    onBackground = White,
    error = Red,
    onError = Black
)

@Composable
fun SirasaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}