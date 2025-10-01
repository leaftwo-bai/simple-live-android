package com.xycz.simplelive.core.platform.bilibili

import com.xycz.simplelive.core.danmaku.LiveDanmaku
import com.xycz.simplelive.core.model.*
import com.xycz.simplelive.core.network.HttpClient
import com.xycz.simplelive.core.platform.LiveSite
import okhttp3.OkHttpClient

/**
 * BiliBili live site implementation
 */
class BiliBiliSite(
    private val okHttpClient: OkHttpClient = HttpClient.createOkHttpClient(enableLogging = true)
) : LiveSite {

    override val id: String = "bilibili"
    override val name: String = "哔哩哔哩直播"

    private val apiService: BiliBiliApiService by lazy {
        HttpClient.createRetrofit(
            baseUrl = BiliBiliApiService.BASE_URL,
            okHttpClient = okHttpClient
        ).create(BiliBiliApiService::class.java)
    }

    private var buvid3: String = ""
    private var buvid4: String = ""
    private var cookie: String = ""

    companion object {
        private const val USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36 Edg/126.0.0.0"
        private const val REFERER = "https://live.bilibili.com/"
    }

    override fun getDanmaku(): LiveDanmaku {
        return com.xycz.simplelive.core.danmaku.bilibili.BiliBiliDanmaku()
    }

    /**
     * Get headers for API requests
     */
    private suspend fun getHeaders(): Map<String, String> {
        if (buvid3.isEmpty()) {
            getBuvidInfo()
        }

        return if (cookie.isEmpty()) {
            mapOf(
                "user-agent" to USER_AGENT,
                "referer" to REFERER,
                "cookie" to "buvid3=$buvid3;buvid4=$buvid4;"
            )
        } else {
            val finalCookie = if (cookie.contains("buvid3")) {
                cookie
            } else {
                "$cookie;buvid3=$buvid3;buvid4=$buvid4;"
            }
            mapOf(
                "cookie" to finalCookie,
                "user-agent" to USER_AGENT,
                "referer" to REFERER
            )
        }
    }

    /**
     * Get buvid device fingerprint
     */
    private suspend fun getBuvidInfo() {
        try {
            val response = apiService.getBuvid()
            if (response.isSuccess()) {
                val data = response.getOrThrow()
                buvid3 = data.b3 ?: ""
                buvid4 = data.b4 ?: ""
            }
        } catch (e: Exception) {
            // Use fallback values
            buvid3 = ""
            buvid4 = ""
        }
    }

    override suspend fun getCategories(): List<LiveCategory> {
        val params = mapOf(
            "need_entrance" to "1",
            "parent_id" to "0"
        )

        val response = apiService.getCategories(params)
        val data = response.getOrThrow()

        return data.map { category ->
            LiveCategory(
                id = category.id,
                name = category.name,
                children = category.list.map { sub ->
                    LiveSubCategory(
                        id = sub.id,
                        name = sub.name,
                        parentId = sub.parentId,
                        pic = sub.pic?.let { "$it@100w.png" }
                    )
                }
            )
        }
    }

    override suspend fun searchRooms(keyword: String, page: Int): LiveSearchRoomResult {
        val params = mapOf(
            "keyword" to keyword,
            "page" to page.toString(),
            "page_size" to "20"
        )

        val response = apiService.searchRooms(params)
        val data = response.getOrThrow()

        val rooms = data.result?.liveRoom?.map { item ->
            LiveRoomItem(
                roomId = item.roomid.toString(),
                title = item.title,
                cover = "${item.cover}@400w.jpg",
                userName = item.uname,
                online = item.online
            )
        } ?: emptyList()

        return LiveSearchRoomResult(
            hasMore = rooms.isNotEmpty(),
            items = rooms
        )
    }

    override suspend fun searchAnchors(keyword: String, page: Int): LiveSearchAnchorResult {
        // BiliBili doesn't have separate anchor search
        return LiveSearchAnchorResult(hasMore = false, items = emptyList())
    }

    override suspend fun getCategoryRooms(
        category: LiveSubCategory,
        page: Int
    ): LiveCategoryResult {
        // TODO: Implement WBI signature for this endpoint
        val params = mutableMapOf(
            "platform" to "web",
            "parent_area_id" to category.parentId,
            "area_id" to category.id,
            "sort_type" to "",
            "page" to page.toString()
        )

        val response = apiService.getCategoryRooms(params)
        val data = response.getOrThrow()

        val rooms = data.list.map { item ->
            LiveRoomItem(
                roomId = item.roomid.toString(),
                title = item.title,
                cover = "${item.cover}@400w.jpg",
                userName = item.uname,
                online = item.online
            )
        }

        return LiveCategoryResult(
            hasMore = data.hasMore == 1,
            items = rooms
        )
    }

    override suspend fun getRecommendRooms(page: Int): LiveCategoryResult {
        val params = mapOf(
            "platform" to "web",
            "sort" to "online",
            "page_size" to "30",
            "page" to page.toString()
        )

        val response = apiService.getRecommendRooms(params)
        val data = response.getOrThrow()

        val rooms = data.list.map { item ->
            LiveRoomItem(
                roomId = item.roomid.toString(),
                title = item.title,
                cover = "${item.cover}@400w.jpg",
                userName = item.uname,
                online = item.online
            )
        }

        return LiveCategoryResult(
            hasMore = rooms.isNotEmpty(),
            items = rooms
        )
    }

    override suspend fun getRoomDetail(roomId: String): LiveRoomDetail {
        val params = mapOf("room_id" to roomId)
        val response = apiService.getRoomInfo(params)
        val data = response.getOrThrow()

        val roomInfo = data.roomInfo
        val anchorInfo = data.anchorInfo?.baseInfo

        return LiveRoomDetail(
            roomId = roomInfo.roomId.toString(),
            title = roomInfo.title,
            cover = roomInfo.cover,
            userName = anchorInfo?.uname ?: "",
            userAvatar = anchorInfo?.face ?: "",
            online = roomInfo.online,
            introduction = roomInfo.description,
            notice = null,
            status = roomInfo.liveStatus == 1,
            url = "https://live.bilibili.com/$roomId",
            data = roomInfo.roomId.toString(), // Store real room ID
            danmakuData = null,
            isRecord = false,
            showTime = roomInfo.liveTime
        )
    }

    override suspend fun getPlayQualities(detail: LiveRoomDetail): List<LivePlayQuality> {
        val params = mapOf(
            "room_id" to detail.roomId,
            "protocol" to "0,1",
            "format" to "0,1,2",
            "codec" to "0,1",
            "platform" to "web"
        )

        val response = apiService.getRoomPlayInfo(params)
        val data = response.getOrThrow()

        // Build quality map
        val qualityMap = data.playurlInfo.playurl.qualityDescriptions.associate {
            it.qn to it.desc
        }

        // Get available qualities from first stream
        val firstStream = data.playurlInfo.playurl.stream.firstOrNull()
        val firstFormat = firstStream?.format?.firstOrNull()
        val firstCodec = firstFormat?.codec?.firstOrNull()
        val acceptQn = firstCodec?.acceptQn ?: emptyList()

        return acceptQn.map { qn ->
            LivePlayQuality(
                quality = qualityMap[qn] ?: "未知清晰度",
                data = qn.toString(),
                sort = qn
            )
        }.sortedByDescending { it.sort }
    }

    override suspend fun getPlayUrls(
        detail: LiveRoomDetail,
        quality: LivePlayQuality
    ): LivePlayUrl {
        val params = mapOf(
            "room_id" to detail.roomId,
            "protocol" to "0,1",
            "format" to "0,2",
            "codec" to "0",
            "platform" to "web",
            "qn" to quality.data.toString()
        )

        val response = apiService.getRoomPlayInfo(params)
        val data = response.getOrThrow()

        val urls = mutableListOf<String>()

        data.playurlInfo.playurl.stream.forEach { stream ->
            stream.format.forEach { format ->
                format.codec.forEach { codec ->
                    val baseUrl = codec.baseUrl
                    codec.urlInfo.forEach { urlInfo ->
                        urls.add("${urlInfo.host}$baseUrl${urlInfo.extra}")
                    }
                }
            }
        }

        // Sort URLs: put mcdn at the end
        urls.sortWith { a, b ->
            when {
                a.contains("mcdn") && !b.contains("mcdn") -> 1
                !a.contains("mcdn") && b.contains("mcdn") -> -1
                else -> 0
            }
        }

        return LivePlayUrl(
            urls = urls,
            headers = mapOf(
                "referer" to "https://live.bilibili.com",
                "user-agent" to USER_AGENT
            )
        )
    }

    override suspend fun getLiveStatus(roomId: String): Boolean {
        return try {
            val detail = getRoomDetail(roomId)
            detail.status
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getSuperChatMessages(roomId: String): List<LiveSuperChatMessage> {
        // BiliBili super chat requires additional API endpoint
        return emptyList()
    }
}