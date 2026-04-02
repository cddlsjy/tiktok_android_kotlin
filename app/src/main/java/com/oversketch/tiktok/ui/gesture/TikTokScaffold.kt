package com.oversketch.tiktok.ui.gesture

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.oversketch.tiktok.ui.theme.ColorPlate
import kotlinx.coroutines.launch

/**
 * FIXED: TikTok scaffold with correct gesture handling
 * Three-page architecture: Left (Search) - Middle (Home) - Right (User)
 */
enum class TikTokPagePosition {
    LEFT, MIDDLE, RIGHT
}

@Composable
fun TikTokScaffold(
    modifier: Modifier = Modifier,
    currentTab: Int = 0,
    initialPage: TikTokPagePosition = TikTokPagePosition.MIDDLE,
    header: @Composable () -> Unit = {},
    tabBar: @Composable () -> Unit = {},
    leftPage: @Composable () -> Unit = {},
    middlePage: @Composable () -> Unit = {},
    rightPage: @Composable () -> Unit = {},
    enableGesture: Boolean = true,
    onPagePositionChange: (TikTokPagePosition) -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
    val coroutineScope = rememberCoroutineScope()

    // Target position (what we want to show)
    var targetPosition by remember { mutableStateOf(initialPage) }

    // Animated offset - this is the source of truth for UI
    val animatedOffsetX = remember { Animatable(0f) }
    val animatedOffsetY = remember { Animatable(0f) }

    // Sync animation with target position
    LaunchedEffect(targetPosition) {
        val targetOffset = when (targetPosition) {
            TikTokPagePosition.LEFT -> screenWidthPx
            TikTokPagePosition.MIDDLE -> 0f
            TikTokPagePosition.RIGHT -> -screenWidthPx
        }
        animatedOffsetX.animateTo(
            targetValue = targetOffset,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        onPagePositionChange(targetPosition)
    }

    // Calculate derived values from animated offset
    val currentOffsetX by animatedOffsetX.asState()
    val currentOffsetY by animatedOffsetY.asState()

    // Scale for left page (0.88 when hidden, 1.0 when shown)
    val leftPageScale = (0.88f + 0.12f * (currentOffsetX / screenWidthPx).coerceIn(0f, 1f))
        .coerceAtLeast(0.88f)

    // Determine which page is visually dominant (for back handling)
    val dominantPage = when {
        currentOffsetX > screenWidthPx * 0.3f -> TikTokPagePosition.LEFT
        currentOffsetX < -screenWidthPx * 0.3f -> TikTokPagePosition.RIGHT
        else -> TikTokPagePosition.MIDDLE
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .then(
                if (enableGesture && currentTab == 0) {
                    Modifier.pointerInput(Unit) {
                        val velocityTracker = VelocityTracker()

                        detectHorizontalDragGestures(
                            onDragStart = { offset ->
                                velocityTracker.resetTracking()
                                velocityTracker.addPosition(0, offset)
                                coroutineScope.launch {
                                    animatedOffsetX.stop()
                                }
                            },
                            onDragEnd = {
                                val velocity = velocityTracker.calculateVelocity().x
                                val currentX = animatedOffsetX.value

                                // Velocity-based or position-based snap
                                val newTarget = when {
                                    // Fast swipe right -> go left (search)
                                    velocity > 1000 && dominantPage == TikTokPagePosition.MIDDLE -> {
                                        TikTokPagePosition.LEFT
                                    }
                                    // Fast swipe left -> go right (user)
                                    velocity < -1000 && dominantPage == TikTokPagePosition.MIDDLE -> {
                                        TikTokPagePosition.RIGHT
                                    }
                                    // Fast swipe left from left -> go middle
                                    velocity < -1000 && dominantPage == TikTokPagePosition.LEFT -> {
                                        TikTokPagePosition.MIDDLE
                                    }
                                    // Fast swipe right from right -> go middle
                                    velocity > 1000 && dominantPage == TikTokPagePosition.RIGHT -> {
                                        TikTokPagePosition.MIDDLE
                                    }
                                    // Position-based
                                    currentX > screenWidthPx * 0.5f -> TikTokPagePosition.LEFT
                                    currentX < -screenWidthPx * 0.5f -> TikTokPagePosition.RIGHT
                                    else -> TikTokPagePosition.MIDDLE
                                }

                                targetPosition = newTarget
                            },
                            onHorizontalDrag = { change, dragAmount ->
                                change.consume()
                                velocityTracker.addPosition(change.uptimeMillis, change.position)

                                val newOffset = when (targetPosition) {
                                    TikTokPagePosition.MIDDLE -> {
                                        (currentOffsetX + dragAmount).coerceIn(-screenWidthPx, screenWidthPx)
                                    }
                                    TikTokPagePosition.LEFT -> {
                                        (currentOffsetX + dragAmount).coerceIn(0f, screenWidthPx)
                                    }
                                    TikTokPagePosition.RIGHT -> {
                                        (currentOffsetX + dragAmount).coerceIn(-screenWidthPx, 0f)
                                    }
                                }

                                coroutineScope.launch {
                                    animatedOffsetX.snapTo(newOffset)
                                }
                            }
                        )
                    }
                } else Modifier
            )
    ) {
        // LEFT PAGE: Search (scales and fades)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = leftPageScale
                    scaleY = leftPageScale
                    alpha = if (currentOffsetX > 0) 1f else 0.5f
                }
                .offset(x = (-screenWidthPx * 0.12f).dp) // Compensate for scale
        ) {
            leftPage()
        }

        // MIDDLE PAGE: Home (main content, moves with gesture)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    translationX = if (currentOffsetX > 0) {
                        currentOffsetX // Follow finger when going left
                    } else {
                        currentOffsetX * 0.2f // Parallax when going right
                    }
                    translationY = currentOffsetY
                }
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                header()
                Box(modifier = Modifier.weight(1f)) {
                    middlePage()
                }
                tabBar()
            }
        }

        // RIGHT PAGE: User (slides in from right)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    translationX = screenWidthPx + currentOffsetX.coerceAtMost(0f)
                }
        ) {
            rightPage()
        }
    }
}