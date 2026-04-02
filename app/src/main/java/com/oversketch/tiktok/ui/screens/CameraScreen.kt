package com.oversketch.tiktok.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.FlashAuto
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.oversketch.tiktok.ui.theme.ColorPlate

/**
 * Camera screen placeholder
 * Mapped from Flutter's cameraPage
 */
@Composable
fun CameraScreen(
    onClose: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Close button
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .statusBarsPadding()
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "关闭",
                tint = ColorPlate.white
            )
        }

        // Camera controls
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Camera options
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.FlashAuto,
                        contentDescription = "闪光灯",
                        tint = ColorPlate.white
                    )
                }
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.FlipCameraAndroid,
                        contentDescription = "切换摄像头",
                        tint = ColorPlate.white
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Record button
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(ColorPlate.white)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "点击拍摄",
                style = MaterialTheme.typography.bodyMedium,
                color = ColorPlate.white
            )
        }
    }
}
