package com.oversketch.tiktok.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = ColorPlate.orange,
    secondary = ColorPlate.white,
    tertiary = ColorPlate.red,
    background = ColorPlate.back1,
    surface = ColorPlate.back2,
    onPrimary = ColorPlate.black,
    onSecondary = ColorPlate.black,
    onTertiary = ColorPlate.white,
    onBackground = ColorPlate.white,
    onSurface = ColorPlate.white,
    error = ColorPlate.red,
    onError = ColorPlate.white
)

@Composable
fun TikTokTheme(
    darkTheme: Boolean = true, // TikTok is always dark
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = TikTokMaterialTypography,
        content = content
    )
}
