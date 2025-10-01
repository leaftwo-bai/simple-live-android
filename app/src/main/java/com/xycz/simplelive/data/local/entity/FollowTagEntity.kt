package com.xycz.simplelive.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Follow tag entity
 * Represents a tag/group for organizing followed users
 */
@Entity(tableName = "follow_tags")
data class FollowTagEntity(
    @PrimaryKey
    val id: String,

    /** Tag name */
    val tag: String,

    /** List of user IDs in this tag */
    val userIds: List<String> = emptyList()
)