package com.oversketch.tiktok.data.repository

import com.oversketch.tiktok.data.model.UserVideo

/**
 * Video repository for fetching video data
 * Mapped from Flutter's UserVideo.fetchVideo()
 */
object VideoRepository {
    private val _videos = mutableListOf<UserVideo>()

    init {
        // Initialize with sample videos
        _videos.addAll(UserVideo.fetchVideos())
    }

    fun getVideos(): List<UserVideo> = _videos.toList()

    fun getVideoByIndex(index: Int): UserVideo? {
        return _videos.getOrNull(index)
    }

    fun getVideoCount(): Int = _videos.size

    fun loadMoreVideos(): List<UserVideo> {
        // Return more sample videos for pagination
        return UserVideo.fetchVideos().take(4)
    }
}
