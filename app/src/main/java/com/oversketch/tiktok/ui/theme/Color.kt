package com.oversketch.tiktok.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Color palette for TikTok app - mapped from Flutter's ColorPlate
 */
object ColorPlate {
    // Color palette
    val orange = Color(0xFFFFC459)
    val yellow = Color(0xFFF1E300)
    val green = Color(0xFF7ED321)
    val red = Color(0xFFEB3838)
    val darkGray = Color(0xFF4A4A4A)
    val gray = Color(0xFF9b9b9b)
    val lightGray = Color(0xFFf5f5f4)
    val black = Color(0xFF000000)
    val white = Color(0xFFFFFFFF)
    val clear = Color(0x00000000)

    // Dark background colors
    val back1 = Color(0xFF1D1F22)  // Main dark background
    val back2 = Color(0xFF121314)  // Slightly darker background

    // With opacity variants
    val white66 = white.copy(alpha = 0.66f)
    val white40 = white.copy(alpha = 0.4f)
    val white30 = white.copy(alpha = 0.3f)
    val white15 = white.copy(alpha = 0.15f)

    // Favorite animation gradient
    val favoriteGradientStart = Color(0xFFEF6F6F)
    val favoriteGradientEnd = Color(0xFFF03E3E)
}

/**
 * Standard sizes mapped from Flutter's SysSize
 */
object SysSize {
    const val avatar = 56f
    const val iconNormal = 24f
    const val iconBig = 40f
    const val big = 16f
    const val normal = 14f
    const val small = 12f
}
