package com.xycz.simplelive.core.platform.bilibili

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * BiliBili category data
 */
@Serializable
data class BiliBiliCategoryData(
    val id: String,
    val name: String,
    val list: List<BiliBiliSubCategoryData>
)

@Serializable
data class BiliBiliSubCategoryData(
    val id: String,
    val name: String,
    @SerialName("parent_id")
    val parentId: String,
    val pic: String? = null
)

/**
 * BiliBili room list data
 */
@Serializable
data class BiliBiliRoomListData(
    val list: List<BiliBiliRoomItemData>,
    @SerialName("has_more")
    val hasMore: Int = 0
)

@Serializable
data class BiliBiliRoomItemData(
    val roomid: Long,
    val title: String,
    val cover: String,
    val uname: String,
    val online: Int = 0
)

/**
 * BiliBili room info data
 */
@Serializable
data class BiliBiliRoomInfoData(
    @SerialName("room_info")
    val roomInfo: BiliBiliRoomInfo,
    @SerialName("anchor_info")
    val anchorInfo: BiliBiliAnchorInfo? = null
)

@Serializable
data class BiliBiliRoomInfo(
    @SerialName("room_id")
    val roomId: Long,
    @SerialName("short_id")
    val shortId: Long = 0,
    val uid: Long,
    val title: String,
    val cover: String,
    @SerialName("live_status")
    val liveStatus: Int,
    val online: Int = 0,
    val description: String? = null,
    @SerialName("live_time")
    val liveTime: String? = null
)

@Serializable
data class BiliBiliAnchorInfo(
    @SerialName("base_info")
    val baseInfo: BiliBiliBaseInfo
)

@Serializable
data class BiliBiliBaseInfo(
    val uname: String,
    val face: String
)

/**
 * BiliBili play info data
 */
@Serializable
data class BiliBiliPlayInfoData(
    @SerialName("playurl_info")
    val playurlInfo: BiliBiliPlayUrlInfo
)

@Serializable
data class BiliBiliPlayUrlInfo(
    val playurl: BiliBiliPlayUrl
)

@Serializable
data class BiliBiliPlayUrl(
    @SerialName("g_qn_desc")
    val qualityDescriptions: List<BiliBiliQualityDesc>,
    val stream: List<BiliBiliStream>
)

@Serializable
data class BiliBiliQualityDesc(
    val qn: Int,
    val desc: String
)

@Serializable
data class BiliBiliStream(
    val protocol: String,
    val format: List<BiliBiliFormat>
)

@Serializable
data class BiliBiliFormat(
    @SerialName("format_name")
    val formatName: String,
    val codec: List<BiliBiliCodec>
)

@Serializable
data class BiliBiliCodec(
    @SerialName("codec_name")
    val codecName: String,
    @SerialName("base_url")
    val baseUrl: String,
    @SerialName("url_info")
    val urlInfo: List<BiliBiliUrlInfo>,
    @SerialName("accept_qn")
    val acceptQn: List<Int>? = null
)

@Serializable
data class BiliBiliUrlInfo(
    val host: String,
    val extra: String
)

/**
 * BiliBili danmaku info data
 */
@Serializable
data class BiliBiliDanmuInfoData(
    val token: String,
    @SerialName("host_list")
    val hostList: List<BiliBiliDanmuHost>
)

@Serializable
data class BiliBiliDanmuHost(
    val host: String,
    val port: Int,
    @SerialName("ws_port")
    val wsPort: Int,
    @SerialName("wss_port")
    val wssPort: Int
)

/**
 * BiliBili search room data
 */
@Serializable
data class BiliBiliSearchRoomData(
    val result: BiliBiliSearchResult? = null
)

@Serializable
data class BiliBiliSearchResult(
    @SerialName("live_room")
    val liveRoom: List<BiliBiliSearchRoomItem>? = null
)

@Serializable
data class BiliBiliSearchRoomItem(
    val roomid: Long,
    val title: String,
    val cover: String,
    val uname: String,
    val online: Int = 0
)

/**
 * BiliBili buvid data
 */
@Serializable
data class BiliBiliBuvidData(
    @SerialName("b_3")
    val b3: String? = null,
    @SerialName("b_4")
    val b4: String? = null
)