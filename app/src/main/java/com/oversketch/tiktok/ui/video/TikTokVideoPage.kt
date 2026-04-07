package com.oversketch.tiktok.ui.video

import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import com.oversketch.tiktok.controller.TikTokVideoController
import com.oversketch.tiktok.ui.theme.ColorPlate

/**
 * IPTV video page component
 * Simplified for TV use
 */
@OptIn(UnstableApi::class)
@Composable
fun TikTokVideoPage(
    modifier: Modifier = Modifier,
    videoController: TikTokVideoController,
    pageIndex: Int = 0,
    isFavorite: Boolean = false,
    hasBottomPadding: Boolean = true,
    aspectRatio: Float = 16f / 9f, // 横屏比例
    hidePauseIcon: Boolean = false,
    onAvatar: () -> Unit = {},
    onFavorite: () -> Unit = {},
    onComment: () -> Unit = {},
    onShare: () -> Unit = {},
    onSingleTap: () -> Unit = {}
) {
    val showPauseIcon by videoController.showPauseIcon.collectAsState()

    // 初始化播放器（只调用一次，但播放器内部可能重建）
    DisposableEffect(videoController) {
        videoController.initialize()
        onDispose {
            videoController.pause()
        }
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
                        resizeMode = androidx.media3.ui.AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        // 直接绑定当前的播放器
                        player = videoController.player
                    }
                },
                update = { playerView ->
                    // 关键：每次重组都重新绑定最新的播放器实例
                    val currentPlayer = videoController.player
                    if (playerView.player != currentPlayer) {
                        playerView.player = currentPlayer
                    }
                },
                modifier = Modifier.fillMaxSize()
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
    }
}