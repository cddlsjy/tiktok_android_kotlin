package com.oversketch.tiktok.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.oversketch.tiktok.ui.theme.ColorPlate
import com.oversketch.tiktok.ui.theme.TikTokTypography

/**
 * User profile screen
 * Mapped from Flutter's userPage
 */
@Composable
fun UserScreen(
    isSelfPage: Boolean = true,
    canPop: Boolean = false,
    onBack: () -> Unit = {},
    onFollow: () -> Unit = {}
) {
    val gradientColors = listOf(
        ColorPlate.orange,
        ColorPlate.red
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorPlate.back1)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (canPop) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "返回",
                            tint = ColorPlate.white
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(48.dp))
                }

                Text(
                    text = "个人主页",
                    style = TikTokTypography.big,
                    color = ColorPlate.white
                )

                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = "更多",
                        tint = ColorPlate.white
                    )
                }
            }

            // Profile info with gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(gradientColors)
                    )
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .background(ColorPlate.white)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Username
                    Text(
                        text = "@用户昵称",
                        style = TikTokTypography.big,
                        color = ColorPlate.white
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Stats
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(32.dp)
                    ) {
                        StatItem("123", "关注")
                        StatItem("1.2w", "粉丝")
                        StatItem("10w", "获赞")
                    }

                    if (!isSelfPage) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onFollow,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ColorPlate.red
                            )
                        ) {
                            Text("关注")
                        }
                    }
                }
            }

            // Tab row
            TabRow(
                selectedTabIndex = 0,
                containerColor = ColorPlate.back2,
                contentColor = ColorPlate.white
            ) {
                Tab(
                    selected = true,
                    onClick = { },
                    icon = {
                        Icon(Icons.Default.GridOn, contentDescription = "作品")
                    }
                )
                Tab(
                    selected = false,
                    onClick = { },
                    icon = {
                        Icon(Icons.Default.Favorite, contentDescription = "喜欢")
                    }
                )
            }

            // Video grid
            val videoList = remember { (1..9).toList() }
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(1.dp),
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                items(videoList) {
                    Box(
                        modifier = Modifier
                            .aspectRatio(9f / 16f)
                            .background(ColorPlate.darkGray)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = TikTokTypography.big,
            color = ColorPlate.white
        )
        Text(
            text = label,
            style = TikTokTypography.small,
            color = ColorPlate.white66
        )
    }
}
