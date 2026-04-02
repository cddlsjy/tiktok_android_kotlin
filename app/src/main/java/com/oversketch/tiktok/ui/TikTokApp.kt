package com.oversketch.tiktok.ui

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.oversketch.tiktok.controller.TikTokVideoListController
import com.oversketch.tiktok.ui.components.TikTokCommentBottomSheet
import com.oversketch.tiktok.ui.components.TikTokHeader
import com.oversketch.tiktok.ui.components.TikTokPageTag
import com.oversketch.tiktok.ui.components.TikTokTabBar
import com.oversketch.tiktok.ui.gesture.TikTokPagePosition
import com.oversketch.tiktok.ui.gesture.TikTokScaffold
import com.oversketch.tiktok.ui.screens.*
import com.oversketch.tiktok.ui.theme.TikTokTheme

/**
 * FIXED: Main TikTok App with working video playback and gestures
 */
@Composable
fun TikTokApp() {
    val context = LocalContext.current

    // Create video controller once
    val videoController = remember {
        TikTokVideoListController(context)
    }

    // UI State
    var currentTab by remember { mutableIntStateOf(0) }
    var pagePosition by remember { mutableStateOf(TikTokPagePosition.MIDDLE) }
    var showCommentSheet by remember { mutableStateOf(false) }

    // Cleanup on dispose
    DisposableEffect(Unit) {
        onDispose {
            videoController.release()
        }
    }

    // Handle back press - return to middle page first
    BackHandler(enabled = pagePosition != TikTokPagePosition.MIDDLE) {
        pagePosition = TikTokPagePosition.MIDDLE
    }

    // Also handle back when on non-home tab
    BackHandler(enabled = currentTab != 0 && pagePosition == TikTokPagePosition.MIDDLE) {
        currentTab = 0
    }

    TikTokTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            TikTokScaffold(
                currentTab = currentTab,
                initialPage = pagePosition,
                header = {
                    if (currentTab == 0) {
                        TikTokHeader(
                            onSearch = {
                                pagePosition = TikTokPagePosition.LEFT
                            }
                        )
                    }
                },
                tabBar = {
                    TikTokTabBar(
                        current = when (currentTab) {
                            0 -> TikTokPageTag.HOME
                            1 -> TikTokPageTag.FOLLOW
                            2 -> TikTokPageTag.MSG
                            else -> TikTokPageTag.ME
                        },
                        hasBackground = currentTab != 0 || pagePosition != TikTokPagePosition.MIDDLE,
                        onTabSwitch = { tag ->
                            currentTab = when (tag) {
                                TikTokPageTag.HOME -> 0
                                TikTokPageTag.FOLLOW -> 1
                                TikTokPageTag.MSG -> 2
                                TikTokPageTag.ME -> 3
                            }
                            // Return to middle when switching tabs
                            if (pagePosition != TikTokPagePosition.MIDDLE) {
                                pagePosition = TikTokPagePosition.MIDDLE
                            }
                        },
                        onAddButton = {
                            // TODO: Open camera
                        }
                    )
                },
                leftPage = {
                    SearchScreen(
                        onBack = { pagePosition = TikTokPagePosition.MIDDLE }
                    )
                },
                middlePage = {
                    // FIXED: Working home screen with video playback
                    HomeScreen(
                        videoController = videoController,
                        currentTab = currentTab,
                        onAvatarClick = {
                            pagePosition = TikTokPagePosition.RIGHT
                        },
                        onShowCommentSheet = {
                            showCommentSheet = true
                        }
                    )
                },
                rightPage = {
                    UserScreen(
                        isSelfPage = false,
                        canPop = true,
                        onBack = { pagePosition = TikTokPagePosition.MIDDLE }
                    )
                },
                enableGesture = currentTab == 0, // Only enable gestures on home tab
                onPagePositionChange = { newPosition ->
                    pagePosition = newPosition
                    // Pause video when leaving home page
                    if (newPosition != TikTokPagePosition.MIDDLE) {
                        videoController.currentPlayer?.pause()
                    } else if (currentTab == 0) {
                        videoController.currentPlayer?.play()
                    }
                }
            )

            // Comment bottom sheet
            if (showCommentSheet) {
                TikTokCommentBottomSheet(
                    onDismiss = { showCommentSheet = false }
                )
            }
        }
    }
}