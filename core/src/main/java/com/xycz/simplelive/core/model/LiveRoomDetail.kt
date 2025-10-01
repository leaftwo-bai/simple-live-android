package com.xycz.simplelive.core.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Live room detail model
 * Contains complete information about a live room
 */
@Serializable
data class LiveRoomDetail(
    /** Room ID */
    val roomId: String,

    /** Room title */
    val title: String,

    /** Cover image URL */
    val cover: String,

    /** Streamer username */
    val userName: String,

    /** Streamer avatar URL */
    val userAvatar: String,

    /** Online viewer count */
    val online: Int,

    /** Room introduction/description */
    val introduction: String? = null,

    /** Room notice/announcement */
    val notice: String? = null,

    /** Live status (true = live, false = offline) */
    val status: Boolean,

    /** Platform-specific additional data */
    val data: String? = null,

    /** Danmaku-related additional data */
    val danmakuData: String? = null,

    /** Room URL */
    val url: String,

    /** Whether this is a recorded stream */
    val isRecord: Boolean = false,

    /** Show time information */
    val showTime: String? = null
)