package com.xycz.simplelive.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * History entity
 * Represents a watched live room
 */
@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey
    val id: String,

    /** Room ID */
    val roomId: String,

    /** Streamer username */
    val userName: String,

    /** Avatar URL */
    val face: String,

    /** Platform/site ID */
    val platform: String,

    /** Last watched timestamp */
    val updateTime: Long
)