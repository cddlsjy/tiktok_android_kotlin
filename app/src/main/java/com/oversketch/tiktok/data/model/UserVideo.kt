package com.oversketch.tiktok.data.model

/**
 * Video data model - mapped from Flutter's UserVideo
 */
data class UserVideo(
    val url: String,
    val image: String,
    val desc: String? = null
) {
    companion object {
        // 更可靠的测试视频URL（全部使用国内或稳定可访问的源）
        private val sampleVideos = listOf(
            "https://ark-project.tos-cn-beijing.volces.com/doc_video/ark_vlm_video_input.mp4",
            "https://ark-project.tos-cn-beijing.volces.com/doc_video/video-understanding.mp4",
            "https://media.w3.org/2010/05/sintel/trailer.mp4",
            "http://vjs.zencdn.net/v/oceans.mp4",
            "https://sample-videos.com/video321/mp4/720/big_buck_bunny_720p_1mb.mp4",
            "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
        )

        private val sampleDescriptions = listOf(
            "#原创 有钱人的生活就是这么朴实无华，且枯燥 #短视频",
            "#搞笑 第一次尝试做饭，差点把厨房烧了 #厨房灾难",
            "#生活 周末在家看电影，享受悠闲时光 #放松",
            "#美食 自己做的披萨，味道还不错 #厨艺展示",
            "#旅行 云南大理之旅，美到窒息 #旅行日记",
            "#健身 坚持锻炼第30天，记录一下 #健身打卡",
            "#宠物 我家小猫咪也太可爱了吧 #猫咪",
            "#学习 今天学到了很多新知识 #每天进步"
        )

        fun fetchVideos(): List<UserVideo> {
            return sampleVideos.mapIndexed { index, url ->
                UserVideo(
                    url = url,
                    image = "",
                    desc = sampleDescriptions[index % sampleDescriptions.size]
                )
            }
        }
    }
}