package com.oversketch.tiktok.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.oversketch.tiktok.ui.theme.ColorPlate
import com.oversketch.tiktok.ui.theme.TikTokTypography

/**
 * Comment bottom sheet
 * Mapped from Flutter's TikTokCommentBottomSheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TikTokCommentBottomSheet(
    onDismiss: () -> Unit = {}
) {
    val sampleComments = remember {
        listOf(
            CommentData("用户1", "这个视频太棒了！", "2小时前", "128"),
            CommentData("用户2", "哈哈哈笑死我了", "3小时前", "86"),
            CommentData("用户3", "学习到了新技能", "5小时前", "42"),
            CommentData("用户4", "支持一下", "6小时前", "15"),
            CommentData("用户5", "期待更多作品", "1天前", "8")
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(ColorPlate.back2)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "2.3万条评论",
                    style = TikTokTypography.normalW,
                    color = ColorPlate.white
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "关闭",
                        tint = ColorPlate.white
                    )
                }
            }

            Divider(color = ColorPlate.darkGray)

            // Comments list
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(sampleComments) { comment ->
                    CommentItem(comment = comment)
                }
            }

            Divider(color = ColorPlate.darkGray)

            // Comment input
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .navigationBarsPadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = "",
                    onValueChange = {},
                    placeholder = {
                        Text(
                            text = "添加评论...",
                            color = ColorPlate.gray
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .background(ColorPlate.back1, RoundedCornerShape(24.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = ColorPlate.back1,
                        unfocusedContainerColor = ColorPlate.back1,
                        focusedTextColor = ColorPlate.white,
                        unfocusedTextColor = ColorPlate.white,
                        cursorColor = ColorPlate.white,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )
            }
        }
    }
}

private data class CommentData(
    val username: String,
    val content: String,
    val time: String,
    val likeCount: String
)

@Composable
private fun CommentItem(comment: CommentData) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Avatar placeholder
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(androidx.compose.foundation.shape.CircleShape)
                .background(ColorPlate.orange)
        )

        Column(modifier = Modifier.weight(1f)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = comment.username,
                    style = TikTokTypography.smallWithOpacity,
                    color = ColorPlate.white66
                )
                Text(
                    text = comment.time,
                    style = TikTokTypography.small,
                    color = ColorPlate.gray
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = comment.content,
                style = TikTokTypography.normal,
                color = ColorPlate.white
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "点赞",
                tint = ColorPlate.gray,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = comment.likeCount,
                style = TikTokTypography.small,
                color = ColorPlate.gray
            )
        }
    }
}
