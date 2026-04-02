package com.oversketch.tiktok.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oversketch.tiktok.controller.TikTokVideoListController
import com.oversketch.tiktok.controller.VideoViewModel
import com.oversketch.tiktok.ui.components.TikTokCommentBottomSheet
import com.oversketch.tiktok.ui.theme.ColorPlate
import com.oversketch.tiktok.ui.video.TikTokVideoPage
import kotlinx.coroutines.flow.collectLatest

/**
 * Home screen with working vertical video pager
 * FIXED: Complete integration with video playback
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    videoController: TikTokVideoListController,
    currentTab: Int = 0,
    onAvatarClick: () -> Unit = {},
    onCommentClick: () -> Unit = {},
    onShowCommentSheet: () -> Unit = {}
) {
    val context = LocalContext.current
    val pagerState = rememberPagerState(pageCount = { videoController.videoCount })
    val coroutineScope = rememberCoroutineScope()

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

    Box(modifier = Modifier.fillMaxSize()) {
        // Video pager - NOW WORKING
        if (videoController.videoCount > 0) {
            VerticalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(ColorPlate.back1)
            ) { page ->
                val player = videoController.playerOfIndex(page)
                val isFavorite = videoController.isFavorite(page)

                if (player != null) {
                    TikTokVideoPage(
                        videoController = player,
                        isFavorite = isFavorite,
                        hasBottomPadding = true,
                        onAvatar = onAvatarClick,
                        onFavorite = {
                            videoController.toggleFavorite(page)
                        },
                        onComment = onShowCommentSheet,
                        onShare = {
                            // TODO: Implement share
                        },
                        onSingleTap = {
                            player.togglePlayPause()
                        }
                    )
                }
            }
        }

        // Tab overlay for non-home tabs
        if (currentTab != 0) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ColorPlate.back1)
            ) {
                when (currentTab) {
                    1 -> FollowScreen()
                    2 -> MsgScreen()
                    3 -> UserScreen(isSelfPage = true)
                    else -> {}
                }
            }
        }
    }
}