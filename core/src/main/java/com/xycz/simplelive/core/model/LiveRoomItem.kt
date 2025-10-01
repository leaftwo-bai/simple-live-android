package com.xycz.simplelive.core.model

import kotlinx.serialization.Serializable

/**
 * Live room item model
 * Represents a live room in list views
 */
@Serializable
data class LiveRoomItem(
    /** Room ID */
    val roomId: String,

    /** Room title */
    val title: String,

    /** Cover image URL */
    val cover: String,

    /** Streamer username */
    val userName: String,

    /** Online viewer count */
    val online: Int = 0
)