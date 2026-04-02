package com.oversketch.tiktok.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.oversketch.tiktok.ui.theme.ColorPlate
import com.oversketch.tiktok.ui.theme.TikTokTypography

/**
 * Follow screen
 * Mapped from Flutter's followPage
 */
@Composable
fun FollowScreen(
    onBack: () -> Unit = {}
) {
    val followList = remember {
        listOf(
            FollowItem("用户1", true),
            FollowItem("用户2", false),
            FollowItem("用户3", true),
            FollowItem("用户4", false),
            FollowItem("用户5", true)
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorPlate.back1)
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(followList) { item ->
            FollowItemRow(
                item = item,
                onFollowClick = { /* Toggle follow */ }
            )
        }
    }
}

private data class FollowItem(
    val username: String,
    val isFollowing: Boolean
)

@Composable
private fun FollowItemRow(
    item: FollowItem,
    onFollowClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(ColorPlate.orange)
            )

            Column {
                Text(
                    text = item.username,
                    style = TikTokTypography.normalW,
                    color = ColorPlate.white
                )
                Text(
                    text = "@${item.username.lowercase()}",
                    style = TikTokTypography.small,
                    color = ColorPlate.gray
                )
            }
        }

        // Follow button
        Button(
            onClick = onFollowClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (item.isFollowing) ColorPlate.darkGray else ColorPlate.red
            ),
            shape = RoundedCornerShape(4.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Text(
                text = if (item.isFollowing) "已关注" else "关注",
                style = TikTokTypography.small
            )
        }
    }
}
