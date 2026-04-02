package com.oversketch.tiktok.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.oversketch.tiktok.ui.theme.ColorPlate
import com.oversketch.tiktok.ui.theme.TikTokTypography

/**
 * TikTok header with search and tab switch
 * Mapped from Flutter's TikTokHeader
 */
@Composable
fun TikTokHeader(
    onSearch: () -> Unit = {}
) {
    var currentSelect by remember { mutableIntStateOf(0) }
    val tabList = listOf("推荐", "本地")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .statusBarsPadding(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Search icon
        Box(
            modifier = Modifier
                .weight(1f)
                .clickable { onSearch() }
                .padding(4.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "搜索",
                tint = ColorPlate.white66,
                modifier = Modifier.size(24.dp)
            )
        }

        // Center tabs
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.Center
        ) {
            tabList.forEachIndexed { index, title ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { currentSelect = index },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        style = if (index == currentSelect) {
                            TikTokTypography.big
                        } else {
                            TikTokTypography.bigWithOpacity
                        },
                        color = ColorPlate.white
                    )
                }
            }
        }

        // TV icon
        Box(
            modifier = Modifier
                .weight(1f)
                .clickable { }
                .padding(4.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                imageVector = Icons.Default.Tv,
                contentDescription = "直播",
                tint = ColorPlate.white66,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
