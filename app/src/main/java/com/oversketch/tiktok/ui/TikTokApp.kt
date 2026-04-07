package com.oversketch.tiktok.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.oversketch.tiktok.controller.TikTokVideoListController
import com.oversketch.tiktok.ui.screens.HomeScreen
import com.oversketch.tiktok.ui.theme.TikTokTheme

/**
 * IPTV TV App with horizontal channel navigation
 */
@Composable
fun TikTokApp() {
    val context = LocalContext.current

    // Create video controller once
    val videoController = remember {
        TikTokVideoListController(context)
    }

    // Cleanup on dispose
    DisposableEffect(Unit) {
        onDispose {
            videoController.release()
        }
    }

    TikTokTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            // Main IPTV screen
            HomeScreen(
                videoController = videoController
            )
        }
    }
}