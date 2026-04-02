package com.oversketch.tiktok.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.oversketch.tiktok.ui.theme.ColorPlate
import com.oversketch.tiktok.ui.theme.TikTokTypography

/**
 * Message screen
 * Mapped from Flutter's msgPage
 */
@Composable
fun MsgScreen(
    onBack: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorPlate.back1)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Title
        Text(
            text = "消息",
            style = TikTokTypography.big,
            color = ColorPlate.white,
            modifier = Modifier.padding(16.dp)
        )

        // Message categories
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MessageCategoryItem(
                icon = Icons.Default.Chat,
                title = "私信",
                onClick = { }
            )
            MessageCategoryItem(
                icon = Icons.Default.Notifications,
                title = "通知",
                onClick = { }
            )
        }

        Divider(
            color = ColorPlate.darkGray,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // Message list
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(messageList) { msg ->
                MessageItem(msg = msg)
            }
        }
    }
}

private data class MessageData(
    val username: String,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int = 0
)

private val messageList = listOf(
    MessageData("好友1", "你好啊！", "刚刚", 2),
    MessageData("好友2", "视频真棒", "10分钟前", 0),
    MessageData("好友3", "谢谢关注", "1小时前", 1),
    MessageData("好友4", "一起合作吧", "昨天", 0),
    MessageData("好友5", "晚安", "2天前", 0)
)

@Composable
private fun MessageCategoryItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
            .background(ColorPlate.back2)
            .clickable { onClick() }
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = ColorPlate.white,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            style = TikTokTypography.small,
            color = ColorPlate.white
        )
    }
}

@Composable
private fun MessageItem(msg: MessageData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 8.dp),
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

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = msg.username,
                    style = TikTokTypography.normalW,
                    color = ColorPlate.white
                )
                Text(
                    text = msg.time,
                    style = TikTokTypography.small,
                    color = ColorPlate.gray
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = msg.lastMessage,
                    style = TikTokTypography.small,
                    color = ColorPlate.gray,
                    modifier = Modifier.weight(1f)
                )
                if (msg.unreadCount > 0) {
                    Box(
                        modifier = Modifier
                            .background(ColorPlate.red, androidx.compose.foundation.shape.CircleShape)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = msg.unreadCount.toString(),
                            color = ColorPlate.white,
                            style = TikTokTypography.small
                        )
                    }
                }
            }
        }
    }
}
