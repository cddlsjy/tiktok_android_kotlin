package com.oversketch.tiktok.controller

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import com.oversketch.tiktok.data.model.UserVideo
import com.oversketch.tiktok.data.repository.IptvRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Video list controller managing multiple video players
 * Mapped from Flutter's TikTokVideoListController
 */
class TikTokVideoListController(
    private val context: Context,
    private val loadMoreCount: Int = 1,
    private val preloadCount: Int = 2,
    private val disposeCount: Int = 2   // ← minmax 用 2，更平滑
) {
    private val _playerList = mutableListOf<TikTokVideoController>()
    private val _currentIndex = MutableStateFlow(0)
    private val _isLoading = MutableStateFlow(false)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _favoriteMap = mutableMapOf<Int, Boolean>()
    val favoriteMap: Map<Int, Boolean> get() = _favoriteMap.toMap()

    private var _onIndexChanged: ((Int) -> Unit)? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    val videoCount: Int get() = _playerList.size

    val currentPlayer: TikTokVideoController?
        get() = _playerList.getOrNull(_currentIndex.value)

    init {
        loadIptvChannels()
    }

    private fun loadIptvChannels() {
        coroutineScope.launch {
            _isLoading.value = true
            try {
                val channels = IptvRepository.getChannels()
                channels.forEach { video ->
                    _playerList.add(TikTokVideoController(context, video))
                }
                
                // 立即准备第一个视频，避免初始黑屏
                if (_playerList.isNotEmpty()) {
                    _playerList[0].initialize()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setOnIndexChangedListener(listener: (Int) -> Unit) {
        _onIndexChanged = listener
    }

    fun loadIndex(targetIndex: Int, reload: Boolean = false) {
        if (!reload && _currentIndex.value == targetIndex) return
        if (targetIndex < 0 || targetIndex >= _playerList.size) return

        val oldIndex = _currentIndex.value
        
        // Pause previous
        if (oldIndex in _playerList.indices && oldIndex != targetIndex) {
            _playerList[oldIndex].apply {
                seekToStart()
                pause()
            }
        }

        // Prepare and play new
        _currentIndex.value = targetIndex
        _playerList[targetIndex].apply {
            if (!isPrepared) initialize()
            play()
        }

        // Preload upcoming
        val preloadRange = (targetIndex + 1)..(targetIndex + preloadCount)
        preloadRange.forEach { i ->
            if (i in _playerList.indices && !_playerList[i].isPrepared) {
                _playerList[i].initialize()
            }
        }

        // Release far videos
        _playerList.forEachIndexed { index, controller ->
            if (index < targetIndex - disposeCount || index > targetIndex + disposeCount) {
                if (index != targetIndex) {
                    controller.release()
                }
            }
        }

        // Load more if needed
        if (_playerList.size - targetIndex <= loadMoreCount + 1) {
            loadMoreVideos()
        }

        _onIndexChanged?.invoke(targetIndex)
    }

    private fun loadMoreVideos() {
        // IPTV频道列表是一次性获取的，不需要加载更多
        // 如果需要刷新频道列表，可以调用loadIptvChannels()
    }

    fun playerOfIndex(index: Int): TikTokVideoController? {
        return _playerList.getOrNull(index)
    }

    fun toggleFavorite(index: Int) {
        _favoriteMap[index] = !(_favoriteMap[index] ?: false)
    }

    fun isFavorite(index: Int): Boolean {
        return _favoriteMap[index] ?: false
    }

    fun release() {
        coroutineScope.cancel()
        _playerList.forEach { it.release() }
        _playerList.clear()
    }
}
