package com.oversketch.tiktok.ui.gesture

import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.oversketch.tiktok.ui.theme.ColorPlate
import kotlin.math.roundToInt
import kotlin.random.Random

/**
 * Video gesture handler with tap to pause and double tap to like
 * Mapped from Flutter's TikTokVideoGesture
 */
@Composable
fun TikTokVideoGesture(
    modifier: Modifier = Modifier,
    onSingleTap: () -> Unit = {},
    onDoubleTap: () -> Unit = {}
) {
    var tapPosition by remember { mutableStateOf(IntOffset.Zero) }
    var showFavoriteAnimation by remember { mutableStateOf(false) }
    var favoritePosition by remember { mutableStateOf(IntOffset.Zero) }

    val favoriteAnimations = remember { mutableStateListOf<FavoriteAnimationState>() }

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { offset ->
                        if (showFavoriteAnimation) {
                            // Double tap - add favorite
                            favoritePosition = IntOffset(offset.x.roundToInt(), offset.y.roundToInt())
                            favoriteAnimations.add(
                                FavoriteAnimationState(
                                    id = System.currentTimeMillis(),
                                    position = favoritePosition
                                )
                            )
                            onDoubleTap()
                        } else {
                            // Single tap - toggle play/pause
                            onSingleTap()
                        }
                        // 600ms delay to distinguish single vs double tap
                        showFavoriteAnimation = true
                    },
                    onDoubleTap = {
                        favoritePosition = IntOffset(it.x.roundToInt(), it.y.roundToInt())
                        favoriteAnimations.add(
                            FavoriteAnimationState(
                                id = System.currentTimeMillis(),
                                position = favoritePosition
                            )
                        )
                        onDoubleTap()
                    }
                )
            }
    ) {
        // Favorite animations
        favoriteAnimations.forEach { animation ->
            FavoriteAnimationIcon(
                position = animation.position,
                onAnimationComplete = {
                    favoriteAnimations.remove(animation)
                }
            )
        }
    }
}

private data class FavoriteAnimationState(
    val id: Long,
    val position: IntOffset
)

@Composable
private fun FavoriteAnimationIcon(
    position: IntOffset,
    onAnimationComplete: () -> Unit
) {
    var animationProgress by remember { mutableFloatStateOf(0f) }
    val animatedScale by animateFloatAsState(
        targetValue = if (animationProgress < 0.1f) {
            1f + (0.1f - animationProgress)
        } else if (animationProgress > 0.8f) {
            1f - (animationProgress - 0.8f) / 0.2f
        } else {
            1f
        },
        animationSpec = tween(durationMillis = 1600, easing = LinearEasing),
        label = "scale"
    )

    val animatedAlpha by animateFloatAsState(
        targetValue = when {
            animationProgress < 0.1f -> animationProgress / 0.1f
            animationProgress > 0.8f -> 1f - (animationProgress - 0.8f) / 0.2f
            else -> 1f
        },
        animationSpec = tween(durationMillis = 1600, easing = LinearEasing),
        label = "alpha"
    )

    val rotation = remember { Random.nextFloat() * 36f - 18f } // -18 to 18 degrees

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(1600)
        onAnimationComplete()
    }

    LaunchedEffect(Unit) {
        val startTime = System.currentTimeMillis()
        while (animationProgress < 1f) {
            animationProgress = (System.currentTimeMillis() - startTime) / 1600f
        }
    }

    Box(
        modifier = Modifier
            .offset { IntOffset(position.x - 50, position.y - 50) }
            .size(100.dp)
            .graphicsLayer {
                scaleX = animatedScale
                scaleY = animatedScale
                alpha = animatedAlpha
                rotationZ = rotation
            }
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "喜欢",
            tint = ColorPlate.red,
            modifier = Modifier.size(100.dp)
        )
    }
}
