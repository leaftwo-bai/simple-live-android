package com.xycz.simplelive.core.platform

import com.xycz.simplelive.core.danmaku.LiveDanmaku
import com.xycz.simplelive.core.model.*

/**
 * Live site interface
 * Abstract interface for all live streaming platforms
 */
interface LiveSite {
    /** Site unique ID */
    val id: String

    /** Site display name */
    val name: String

    /**
     * Get danmaku client for this site
     */
    fun getDanmaku(): LiveDanmaku

    /**
     * Get all categories for this site
     */
    suspend fun getCategories(): List<LiveCategory>

    /**
     * Search for live rooms
     * @param keyword Search keyword
     * @param page Page number (1-based)
     */
    suspend fun searchRooms(keyword: String, page: Int = 1): LiveSearchRoomResult

    /**
     * Search for anchors/streamers
     * @param keyword Search keyword
     * @param page Page number (1-based)
     */
    suspend fun searchAnchors(keyword: String, page: Int = 1): LiveSearchAnchorResult

    /**
     * Get rooms in a category
     * @param category Subcategory to browse
     * @param page Page number (1-based)
     */
    suspend fun getCategoryRooms(category: LiveSubCategory, page: Int = 1): LiveCategoryResult

    /**
     * Get recommended rooms
     * @param page Page number (1-based)
     */
    suspend fun getRecommendRooms(page: Int = 1): LiveCategoryResult

    /**
     * Get room detail
     * @param roomId Room ID
     */
    suspend fun getRoomDetail(roomId: String): LiveRoomDetail

    /**
     * Get available play qualities
     * @param detail Room detail
     */
    suspend fun getPlayQualities(detail: LiveRoomDetail): List<LivePlayQuality>

    /**
     * Get play URLs for a specific quality
     * @param detail Room detail
     * @param quality Selected quality
     */
    suspend fun getPlayUrls(detail: LiveRoomDetail, quality: LivePlayQuality): LivePlayUrl

    /**
     * Get live status
     * @param roomId Room ID
     * @return true if live, false if offline
     */
    suspend fun getLiveStatus(roomId: String): Boolean

    /**
     * Get super chat messages
     * @param roomId Room ID
     */
    suspend fun getSuperChatMessages(roomId: String): List<LiveSuperChatMessage>
}