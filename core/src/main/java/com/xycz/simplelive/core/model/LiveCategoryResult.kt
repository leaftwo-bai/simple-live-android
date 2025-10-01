package com.xycz.simplelive.core.model

import kotlinx.serialization.Serializable

/**
 * Live category result model
 * Contains paginated results for a category
 */
@Serializable
data class LiveCategoryResult(
    /** Whether there are more results available */
    val hasMore: Boolean,

    /** List of room items */
    val items: List<LiveRoomItem>
)