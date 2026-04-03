package com.oversketch.tiktok.controller

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.oversketch.tiktok.data.model.UserVideo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ExoPlayer wrapper for video playback
 * Mapped from Flutter's VPVideoController
 */
@OptIn(UnstableApi::class)
class TikTokVideoController(
    private val context: Context,
    val videoInfo: UserVideo
) {
    private val _showPauseIcon = MutableStateFlow(false)
    val showPauseIcon: StateFlow<Boolean> = _showPauseIcon.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private var _isPrepared = false
    val isPrepared: Boolean get() = _isPrepared

    private var _player: ExoPlayer? = null
    val player: ExoPlayer
        get() {
            if (_player == null) {
                _player = ExoPlayer.Builder(context).build().apply {
                    val mediaItem = MediaItem.fromUri(videoInfo.url)
                    setMediaItem(mediaItem)
                    repeatMode = Player.REPEAT_MODE_ONE
                    addListener(object : Player.Listener {
                        override fun onIsPlayingChanged(isPlaying: Boolean) {
                            _isPlaying.value = isPlaying
                            if (isPlaying) {
                                _showPauseIcon.value = false
                            }
                        }

                        override fun onPlaybackStateChanged(playbackState: Int) {
                            if (playbackState == Player.STATE_READY) {
                                _isPrepared = true
                            }
                        }
                        
                        // 添加错误处理
                        override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                            android.util.Log.e("TikTokVideoController", "播放错误: ${error.message}")
                        }
                    })
                }
            }
            return _player!!
        }

    fun initialize() {
        if (!_isPrepared) {
            player.prepare()
            _isPrepared = true
        }
        player.playWhenReady = true
    }

    fun play() {
        if (!_isPrepared) {
            initialize()
        }
        player.play()
        _showPauseIcon.value = false
    }

    fun pause(showPauseIcon: Boolean = false) {
        player.pause()
        if (showPauseIcon) {
            _showPauseIcon.value = true
        }
    }

    fun seekToStart() {
        player.seekTo(0)
    }

    fun release() {
        _player?.release()
        _player = null
        _isPrepared = false
    }

    fun togglePlayPause() {
        if (_isPlaying.value) {
            pause(showPauseIcon = true)
        } else {
            play()
        }
    }
}
