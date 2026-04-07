package com.oversketch.tiktok.data.repository

import android.util.Log
import com.oversketch.tiktok.data.model.UserVideo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * IPTV频道仓库
 * 从iptv-org获取中文频道列表并解析M3U格式
 */
object IptvRepository {
    private const val TAG = "IptvRepository"
    private const val M3U_URL = "https://iptv-org.github.io/iptv/languages/zho.m3u"
    private const val CACHE_DURATION_MS = 6 * 60 * 60 * 1000 // 6小时缓存
    private const val MAX_CHANNELS = 500 // 限制最大频道数量

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private var cachedChannels: List<UserVideo>? = null
    private var lastFetchTime: Long = 0

    /**
     * 获取IPTV频道列表
     * 优先使用缓存，缓存过期后重新获取
     */
    suspend fun getChannels(): List<UserVideo> = withContext(Dispatchers.IO) {
        val currentTime = System.currentTimeMillis()
        
        // 检查缓存是否有效
        if (cachedChannels != null && (currentTime - lastFetchTime) < CACHE_DURATION_MS) {
            Log.d(TAG, "Using cached channels")
            return@withContext cachedChannels!!
        }

        try {
            Log.d(TAG, "Fetching M3U from: $M3U_URL")
            val channels = fetchAndParseM3U()
            cachedChannels = channels
            lastFetchTime = currentTime
            Log.d(TAG, "Fetched ${channels.size} channels")
            return@withContext channels
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch IPTV channels: ${e.message}")
            // 返回备用本地测试视频
            return@withContext getFallbackVideos()
        }
    }

    /**
     * 从网络获取并解析M3U文件
     */
    private fun fetchAndParseM3U(): List<UserVideo> {
        val request = Request.Builder()
            .url(M3U_URL)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw IOException("Unexpected code $response")
            }

            val m3uContent = response.body?.string() ?: throw IOException("Empty response")
            return parseM3U(m3uContent)
        }
    }

    /**
     * 解析M3U格式内容
     * 提取频道名称和URL
     */
    private fun parseM3U(content: String): List<UserVideo> {
        val lines = content.lines()
        val channels = mutableListOf<UserVideo>()
        var currentChannelName: String? = null

        for (line in lines) {
            val trimmedLine = line.trim()
            
            if (trimmedLine.startsWith("#EXTINF:")) {
                // 解析频道信息行
                currentChannelName = parseChannelName(trimmedLine)
            } else if (trimmedLine.isNotEmpty() && !trimmedLine.startsWith("#")) {
                // 解析频道URL行
                if (currentChannelName != null && trimmedLine.startsWith("http")) {
                    channels.add(UserVideo(
                        url = trimmedLine,
                        image = "",
                        desc = currentChannelName
                    ))
                    
                    // 限制频道数量
                    if (channels.size >= MAX_CHANNELS) {
                        break
                    }
                }
                currentChannelName = null
            }
        }

        return channels
    }

    /**
     * 从EXTINF行解析频道名称
     */
    private fun parseChannelName(extinfLine: String): String {
        // 尝试从tvg-name属性提取
        val tvgNameMatch = "tvg-name=\"([^\"]+)\"" .toRegex().find(extinfLine)
        if (tvgNameMatch != null) {
            return tvgNameMatch.groupValues[1]
        }

        // 从逗号后提取
        val commaIndex = extinfLine.lastIndexOf(',')
        if (commaIndex != -1 && commaIndex < extinfLine.length - 1) {
            return extinfLine.substring(commaIndex + 1).trim()
        }

        // 默认名称
        return "未知频道"
    }

    /**
     * 备用本地测试视频源
     * 当网络请求失败时使用
     */
    private fun getFallbackVideos(): List<UserVideo> {
        Log.d(TAG, "Using fallback videos")
        return listOf(
            UserVideo(
                url = "https://ark-project.tos-cn-beijing.volces.com/doc_video/ark_vlm_video_input.mp4",
                image = "",
                desc = "测试频道 1"
            ),
            UserVideo(
                url = "https://ark-project.tos-cn-beijing.volces.com/doc_video/video-understanding.mp4",
                image = "",
                desc = "测试频道 2"
            ),
            UserVideo(
                url = "https://media.w3.org/2010/05/sintel/trailer.mp4",
                image = "",
                desc = "测试频道 3"
            ),
            UserVideo(
                url = "http://vjs.zencdn.net/v/oceans.mp4",
                image = "",
                desc = "测试频道 4"
            )
        )
    }

    /**
     * 清除缓存
     */
    fun clearCache() {
        cachedChannels = null
        lastFetchTime = 0
    }
}