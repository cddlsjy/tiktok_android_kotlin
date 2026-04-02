package com.oversketch.tiktok.ui.video

import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.oversketch.tiktok.controller.TikTokVideoController
import com.oversketch.tiktok.ui.components.TikTokButtonColumn
import com.oversketch.tiktok.ui.components.VideoUserInfo
import com.oversketch.tiktok.ui.gesture.TikTokVideoGesture
import com.oversketch.tiktok.ui.theme.ColorPlate

/**
 * TikTok video page component
 * Mapped from Flutter's TikTokVideoPage
 */
@Composable
fun TikTokVideoPage(
    modifier: Modifier = Modifier,
    videoController: TikTokVideoController,
    isFavorite: Boolean = false,
    hasBottomPadding: Boolean = true,
    aspectRatio: Float = 9f / 16f,
    hidePauseIcon: Boolean = false,
    onAvatar: () -> Unit = {},
    onFavorite: () -> Unit = {},
    onComment: () -> Unit = {},
    onShare: () -> Unit = {},
    onSingleTap: () -> Unit = {}
) {
    val isPlaying by videoController.isPlaying.collectAsState()
    val showPauseIcon by videoController.showPauseIcon.collectAsState()

    val context = LocalContext.current
    val exoPlayer = remember(videoController) { videoController.player }

    // Initialize player
    DisposableEffect(videoController) {
        videoController.initialize()
        onDispose { }
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Video layer
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        useController = false
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                },
                update = { playerView ->
                    playerView.player = exoPlayer
                },
                modifier = Modifier.fillMaxSize()
            )

            // Gesture layer
            TikTokVideoGesture(
                modifier = Modifier.fillMaxSize(),
                onSingleTap = onSingleTap,
                onDoubleTap = onFavorite
            )

            // Pause icon overlay
            if (showPauseIcon && !hidePauseIcon) {
                Icon(
                    imageVector = Icons.Default.PlayCircleOutline,
                    contentDescription = "暂停",
                    tint = ColorPlate.white40,
                    modifier = Modifier.size(120.dp)
                )
            }
        }

        // Right buttons
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (hasBottomPadding) 16.dp else 0.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            TikTokButtonColumn(
                bottomPadding = if (hasBottomPadding) 50f else 16f,
                isFavorite = isFavorite,
                onAvatar = onAvatar,
                onFavorite = onFavorite,
                onComment = onComment,
                onShare = onShare
            )
        }

        // User info
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (hasBottomPadding) 66.dp else 0.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            VideoUserInfo(
                description = videoController.videoInfo.desc,
                bottomPadding = if (hasBottomPadding) 50f else 16f
            )
        }
    }
}
