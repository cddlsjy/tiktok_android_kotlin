package com.oversketch.tiktok.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.oversketch.tiktok.ui.theme.ColorPlate
import com.oversketch.tiktok.ui.theme.TikTokTypography

/**
 * Search screen - slides in from left
 * Mapped from Flutter's searchPage
 */
@Composable
fun SearchScreen(
    onBack: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    val searchHistory = remember { listOf("热门搜索1", "热门搜索2", "热门搜索3") }
    val searchResults = remember { listOf("结果1", "结果2", "结果3") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorPlate.back1)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Search bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "返回",
                    tint = ColorPlate.white
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(ColorPlate.back2)
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "搜索",
                        tint = ColorPlate.gray,
                        modifier = Modifier.size(20.dp)
                    )
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        singleLine = true,
                        textStyle = TikTokTypography.normal.copy(ColorPlate.white),
                        cursorBrush = SolidColor(ColorPlate.white),
                        modifier = Modifier.weight(1f),
                        decorationBox = { innerTextField ->
                            Box {
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        text = "搜索用户、视频",
                                        style = TikTokTypography.normal,
                                        color = ColorPlate.gray
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { searchQuery = "" },
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "清除",
                                tint = ColorPlate.gray
                            )
                        }
                    }
                }
            }
        }

        // Search history / results
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (searchQuery.isEmpty()) {
                // Hot search section
                item {
                    Text(
                        text = "热门搜索",
                        style = TikTokTypography.normalW,
                        color = ColorPlate.white
                    )
                }
                items(searchHistory) { item ->
                    SearchHistoryItem(text = item)
                }
            } else {
                // Search results
                items(searchResults) { item ->
                    SearchResultItem(
                        username = item,
                        onClick = { /* Navigate to user */ }
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchHistoryItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            style = TikTokTypography.normal,
            color = ColorPlate.white
        )
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = ColorPlate.gray,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
private fun SearchResultItem(
    username: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(ColorPlate.orange)
        )
        Column {
            Text(
                text = username,
                style = TikTokTypography.normalW,
                color = ColorPlate.white
            )
            Text(
                text = "@${username.lowercase()}",
                style = TikTokTypography.small,
                color = ColorPlate.gray
            )
        }
    }
}
