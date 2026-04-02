package com.oversketch.tiktok.controller

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing video state across the app
 * Stage 5: Data and Business Logic
 */
class VideoViewModel(application: Application) : AndroidViewModel(application) {

    private val _videoListController = MutableStateFlow<TikTokVideoListController?>(null)
    val videoListController: StateFlow<TikTokVideoListController?> = _videoListController.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            _videoListController.value = TikTokVideoListController(application)
        }
    }

    fun loadIndex(index: Int) {
        _videoListController.value?.loadIndex(index)
        _currentIndex.value = index
    }

    fun toggleFavorite(index: Int) {
        _videoListController.value?.toggleFavorite(index)
    }

    fun isFavorite(index: Int): Boolean {
        return _videoListController.value?.isFavorite(index) ?: false
    }

    fun playCurrent() {
        _videoListController.value?.currentPlayer?.play()
    }

    fun pauseCurrent() {
        _videoListController.value?.currentPlayer?.pause()
    }

    override fun onCleared() {
        super.onCleared()
        _videoListController.value?.release()
    }
}
