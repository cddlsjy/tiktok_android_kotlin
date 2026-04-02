package com.oversketch.tiktok.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.oversketch.tiktok.ui.theme.ColorPlate
import com.oversketch.tiktok.ui.theme.TikTokTypography

/**
 * TikTok bottom navigation bar
 * Mapped from Flutter's TikTokTabBar
 */
enum class TikTokPageTag {
    HOME, FOLLOW, MSG, ME
}

@Composable
fun TikTokTabBar(
    current: TikTokPageTag = TikTokPageTag.HOME,
    hasBackground: Boolean = false,
    onTabSwitch: (TikTokPageTag) -> Unit = {},
    onAddButton: () -> Unit = {}
) {
    val backgroundColor = if (hasBackground) {
        ColorPlate.back2.copy(alpha = 0.95f)
    } else {
        Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(bottom = 16.dp)
            .statusBarsPadding()
            .height(50.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TabItem(
            icon = Icons.Default.Home,
            label = "首页",
            isSelected = current == TikTokPageTag.HOME,
            modifier = Modifier.weight(1f),
            onClick = { onTabSwitch(TikTokPageTag.HOME) }
        )

        TabItem(
            icon = Icons.Default.Favorite,
            label = "关注",
            isSelected = current == TikTokPageTag.FOLLOW,
            modifier = Modifier.weight(1f),
            onClick = { onTabSwitch(TikTokPageTag.FOLLOW) }
        )

        // Add button in the middle
        Box(
            modifier = Modifier
                .weight(1f)
                .clickable { onAddButton() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AddBox,
                contentDescription = "添加",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }

        TabItem(
            icon = Icons.Default.Message,
            label = "消息",
            isSelected = current == TikTokPageTag.MSG,
            modifier = Modifier.weight(1f),
            onClick = { onTabSwitch(TikTokPageTag.MSG) }
        )

        TabItem(
            icon = Icons.Default.Person,
            label = "我",
            isSelected = current == TikTokPageTag.ME,
            modifier = Modifier.weight(1f),
            onClick = { onTabSwitch(TikTokPageTag.ME) }
        )
    }
}

@Composable
private fun TabItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (isSelected) "●" else "",
            color = Color.White,
            fontSize = androidx.compose.ui.unit.TextUnit.Unspecified
        )
        Text(
            text = label,
            style = if (isSelected) TikTokTypography.normalW else TikTokTypography.normalWithOpacity,
            color = Color.White
        )
    }
}
