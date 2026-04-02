package com.oversketch.tiktok.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.oversketch.tiktok.ui.theme.ColorPlate
import com.oversketch.tiktok.ui.theme.TikTokTypography

/**
 * User info at bottom left of video
 * Mapped from Flutter's VideoUserInfo
 */
@Composable
fun VideoUserInfo(
    modifier: Modifier = Modifier,
    username: String = "@朱二旦的枯燥生活",
    description: String? = "#原创 有钱人的生活就是这么朴实无华，且枯燥 #短视频",
    musicInfo: String = "朱二旦的枯燥生活创作的原声",
    bottomPadding: Float = 50f
) {
    Column(
        modifier = modifier
            .padding(start = 12.dp, bottom = bottomPadding.dp)
            .padding(end = 80.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        // Username
        Text(
            text = username,
            style = TikTokTypography.big,
            color = ColorPlate.white
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Description
        Text(
            text = description ?: "",
            style = TikTokTypography.normal,
            color = ColorPlate.white,
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Music info with marquee effect placeholder
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.MusicNote,
                contentDescription = "音乐",
                tint = ColorPlate.white,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = musicInfo,
                style = TikTokTypography.normal,
                color = ColorPlate.white,
                maxLines = 1
            )
        }
    }
}
