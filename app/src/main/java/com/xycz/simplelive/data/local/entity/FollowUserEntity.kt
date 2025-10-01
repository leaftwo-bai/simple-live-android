package com.xycz.simplelive.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Follow user entity
 * Represents a followed streamer
 */
@Entity(tableName = "follow_users")
data class FollowUserEntity(
    @PrimaryKey
    val id: String,

    /** Room ID */
    val roomId: String,

    /** Username */
    val userName: String,

    /** Avatar URL */
    val face: String,

    /** Current live status */
    val liveStatus: Boolean,

    /** Timestamp when followed */
    val addTime: Long,

    /** Platform/site ID (bilibili, douyu, etc.) */
    val siteId: String,

    /** List of tag IDs this user belongs to */
    val tagIds: List<String> = emptyList()
)