package com.oversketch.tiktok.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.focus.focusable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oversketch.tiktok.controller.TikTokVideoListController
import com.oversketch.tiktok.ui.theme.ColorPlate
import com.oversketch.tiktok.ui.video.TikTokVideoPage
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Home screen with horizontal pager for IPTV channels
 * Support remote control (D-pad) navigation
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    videoController: TikTokVideoListController
) {
    val pagerState = rememberPagerState(pageCount = { videoController.videoCount })
    val coroutineScope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val isLoading by videoController.isLoading.collectAsState()

    // Sync pager with video controller
    LaunchedEffect(pagerState.currentPage) {
        videoController.loadIndex(pagerState.currentPage)
    }

    // Handle page changes from controller (if any)
    LaunchedEffect(Unit) {
        videoController.currentIndex.collectLatest { index ->
            if (index != pagerState.currentPage && index in 0 until videoController.videoCount) {
                pagerState.animateScrollToPage(index)
            }
        }
    }

    // Request focus on mount
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorPlate.back1)
            .focusRequester(focusRequester)
            .focusable()
            .onKeyEvent { event ->
                when (event.type) {
                    KeyEventType.KeyDown -> {
                        when (event.key) {
                            Key.DirectionUp, Key.DirectionDown -> {
                                // 上下键切换频道
                                val currentPage = pagerState.currentPage
                                val newPage = when (event.key) {
                                    Key.DirectionUp -> currentPage - 1
                                    Key.DirectionDown -> currentPage + 1
                                    else -> currentPage
                                }
                                if (newPage in 0 until videoController.videoCount) {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(newPage)
                                    }
                                    true
                                } else {
                                    false
                                }
                            }
                            Key.DirectionLeft, Key.DirectionRight -> {
                                // 左右键快进/快退
                                videoController.currentPlayer?.let {player ->
                                    val currentPosition = player.player.currentPosition
                                    val newPosition = when (event.key) {
                                        Key.DirectionLeft -> currentPosition - 10000 // 快退10秒
                                        Key.DirectionRight -> currentPosition + 10000 // 快进10秒
                                        else -> currentPosition
                                    }
                                    player.player.seekTo(newPosition.coerceAtLeast(0))
                                    true
                                } ?: false
                            }
                            Key.Enter, Key.Spacebar -> {
                                // 确认键/空格键 播放/暂停
                                videoController.currentPlayer?.togglePlayPause()
                                true
                            }
                            else -> false
                        }
                    }
                    else -> false
                }
            }
    ) {
        // Loading indicator
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        color = ColorPlate.white,
                        modifier = Modifier.size(60.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "正在加载频道列表...",
                        color = ColorPlate.white,
                        fontSize = 18.sp
                    )
                }
            }
        } else if (videoController.videoCount > 0) {
            // Video pager - Horizontal for TV
            HorizontalPager(
                state = pagerState,
                key = { page -> page },
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val player = videoController.playerOfIndex(page)

                if (player != null) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        TikTokVideoPage(
                            videoController = player,
                            pageIndex = page,
                            isFavorite = false, // IPTV不需要收藏功能
                            hasBottomPadding = false,
                            onAvatar = {},
                            onFavorite = {},
                            onComment = {},
                            onShare = {},
                            onSingleTap = {
                                player.togglePlayPause()
                            }
                        )

                        // Channel info overlay
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(16.dp)
                                .background(ColorPlate.back1.copy(alpha = 0.8f))
                                .clip(RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = player.videoInfo.desc ?: "未知频道",
                                color = ColorPlate.white,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        } else {
            // Empty state
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "暂无可用频道",
                    color = ColorPlate.white,
                    fontSize = 20.sp
                )
            }
        }
    }
}