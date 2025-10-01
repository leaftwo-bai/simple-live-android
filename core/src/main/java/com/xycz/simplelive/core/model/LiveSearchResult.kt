package com.xycz.simplelive.core.model

import kotlinx.serialization.Serializable

/**
 * Live search room result model
 * Contains paginated search results for rooms
 */
@Serializable
data class LiveSearchRoomResult(
    /** Whether there are more results available */
    val hasMore: Boolean,

    /** List of room items */
    val items: List<LiveRoomItem>
)

/**
 * Live anchor item model
 * Represents a streamer/anchor in search results
 */
@Serializable
data class LiveAnchorItem(
    /** Room ID */
    val roomId: String,

    /** Streamer username */
    val userName: String,

    /** Avatar URL */
    val avatar: String,

    /** Number of followers */
    val followers: Int = 0,

    /** Whether currently live */
    val isLive: Boolean = false
)

/**
 * Live search anchor result model
 * Contains paginated search results for anchors/streamers
 */
@Serializable
data class LiveSearchAnchorResult(
    /** Whether there are more results available */
    val hasMore: Boolean,

    /** List of anchor items */
    val items: List<LiveAnchorItem>
)