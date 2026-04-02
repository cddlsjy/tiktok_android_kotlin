package com.oversketch.tiktok.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.oversketch.tiktok.ui.theme.ColorPlate
import com.oversketch.tiktok.ui.theme.SysSize
import com.oversketch.tiktok.ui.theme.TikTokTypography

/**
 * Right side button column for video page
 * Mapped from Flutter's TikTokButtonColumn
 */
@Composable
fun TikTokButtonColumn(
    modifier: Modifier = Modifier,
    bottomPadding: Float = 50f,
    isFavorite: Boolean = false,
    commentCount: String = "4213",
    shareCount: String = "346",
    onAvatar: () -> Unit = {},
    onFavorite: () -> Unit = {},
    onComment: () -> Unit = {},
    onShare: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .width(SysSize.avatar.dp)
            .padding(end = 12.dp)
            .padding(bottom = bottomPadding.dp),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ) {
        // Avatar with add button
        TikTokAvatar(onTap = onAvatar)

        // Favorite button
        ActionButton(
            icon = Icons.Default.Favorite,
            text = "1.0w",
            isFavorite = isFavorite,
            onTap = onFavorite
        )

        // Comment button
        ActionButton(
            icon = Icons.Default.Comment,
            text = commentCount,
            onTap = onComment
        )

        // Share button
        ActionButton(
            icon = Icons.Default.Share,
            text = shareCount,
            onTap = onShare
        )

        // Music disc
        MusicDisc()
    }
}

@Composable
private fun TikTokAvatar(onTap: () -> Unit) {
    Box(
        modifier = Modifier
            .size(SysSize.avatar.dp)
            .padding(bottom = 10.dp)
            .clickable { onTap() }
    ) {
        AsyncImage(
            model = "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif",
            contentDescription = "用户头像",
            modifier = Modifier
                .size(SysSize.avatar.dp)
                .clip(CircleShape)
                .border(1.dp, Color.White, CircleShape),
            contentScale = ContentScale.Crop
        )

        // Add button
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 6.dp)
                .size(20.dp)
                .background(ColorPlate.orange, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "关注",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun ActionButton(
    icon: ImageVector,
    text: String,
    isFavorite: Boolean = false,
    onTap: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .clickable { onTap() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isFavorite) ColorPlate.red else ColorPlate.white,
            modifier = Modifier.size(SysSize.iconBig.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = text,
            style = TikTokTypography.small,
            color = ColorPlate.white
        )
    }
}

@Composable
private fun MusicDisc() {
    Box(
        modifier = Modifier
            .padding(top = 10.dp)
            .size(SysSize.avatar.dp)
            .clip(RoundedCornerShape(SysSize.avatar.dp / 2))
            .background(ColorPlate.back1)
    ) {
        // Music disc icon would go here
    }
}
