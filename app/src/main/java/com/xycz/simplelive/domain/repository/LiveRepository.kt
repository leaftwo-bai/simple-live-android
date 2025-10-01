package com.xycz.simplelive.domain.repository

import com.xycz.simplelive.core.model.*

/**
 * Repository interface for live streaming operations
 */
interface LiveRepository {

    /**
     * Get all available sites/platforms
     */
    suspend fun getAllSites(): List<SiteModel>

    /**
     * Get categories for a specific site
     */
    suspend fun getCategories(siteId: String): Result<List<LiveCategory>>

    /**
     * Get recommended rooms for a specific site
     */
    suspend fun getRecommendRooms(siteId: String, page: Int = 1): Result<LiveCategoryResult>

    /**
     * Get rooms in a category
     */
    suspend fun getCategoryRooms(
        siteId: String,
        category: LiveSubCategory,
        page: Int = 1
    ): Result<LiveCategoryResult>

    /**
     * Search for rooms
     */
    suspend fun searchRooms(
        siteId: String,
        keyword: String,
        page: Int = 1
    ): Result<LiveSearchRoomResult>

    /**
     * Get room detail
     */
    suspend fun getRoomDetail(siteId: String, roomId: String): Result<LiveRoomDetail>

    /**
     * Get play qualities for a room
     */
    suspend fun getPlayQualities(
        siteId: String,
        detail: LiveRoomDetail
    ): Result<List<LivePlayQuality>>

    /**
     * Get play URLs for a specific quality
     */
    suspend fun getPlayUrls(
        siteId: String,
        detail: LiveRoomDetail,
        quality: LivePlayQuality
    ): Result<LivePlayUrl>

    /**
     * Get live status
     */
    suspend fun getLiveStatus(siteId: String, roomId: String): Result<Boolean>
}

/**
 * Site model for domain layer
 */
data class SiteModel(
    val id: String,
    val name: String,
    val logo: String? = null
)