package com.oversketch.tiktok.util

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Density

/**
 * Physics utilities mapped from Flutter's physics.dart
 * Custom scroll physics for TikTok-style quick scrolling
 */
object TikTokScrollPhysics {
    const val SCROLL_SPEED = 300
}

/**
 * Extension to convert dp to pixels
 */
fun Density.dpToPx(dp: androidx.compose.ui.unit.Dp): Float {
    return dp.toPx()
}