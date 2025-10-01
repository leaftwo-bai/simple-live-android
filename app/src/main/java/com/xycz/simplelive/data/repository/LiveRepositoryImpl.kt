package com.xycz.simplelive.data.repository

import com.xycz.simplelive.core.model.*
import com.xycz.simplelive.core.platform.LiveSite
import com.xycz.simplelive.core.platform.bilibili.BiliBiliSite
import com.xycz.simplelive.domain.repository.LiveRepository
import com.xycz.simplelive.domain.repository.SiteModel
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of LiveRepository
 */
@Singleton
class LiveRepositoryImpl @Inject constructor(
    // Inject OkHttpClient or other dependencies as needed
) : LiveRepository {

    // Map of site IDs to LiveSite implementations
    private val sites: Map<String, LiveSite> = mapOf(
        "bilibili" to BiliBiliSite(),
        // TODO: Add other sites
        // "douyu" to DouyuSite(),
        // "huya" to HuyaSite(),
        // "douyin" to DouyinSite()
    )

    override suspend fun getAllSites(): List<SiteModel> {
        return listOf(
            SiteModel("bilibili", "哔哩哔哩"),
            SiteModel("douyu", "斗鱼直播"),
            SiteModel("huya", "虎牙直播"),
            SiteModel("douyin", "抖音直播")
        )
    }

    override suspend fun getCategories(siteId: String): Result<List<LiveCategory>> {
        return runCatching {
            val site = getSite(siteId)
            site.getCategories()
        }
    }

    override suspend fun getRecommendRooms(
        siteId: String,
        page: Int
    ): Result<LiveCategoryResult> {
        return runCatching {
            val site = getSite(siteId)
            site.getRecommendRooms(page)
        }
    }

    override suspend fun getCategoryRooms(
        siteId: String,
        category: LiveSubCategory,
        page: Int
    ): Result<LiveCategoryResult> {
        return runCatching {
            val site = getSite(siteId)
            site.getCategoryRooms(category, page)
        }
    }

    override suspend fun searchRooms(
        siteId: String,
        keyword: String,
        page: Int
    ): Result<LiveSearchRoomResult> {
        return runCatching {
            val site = getSite(siteId)
            site.searchRooms(keyword, page)
        }
    }

    override suspend fun getRoomDetail(
        siteId: String,
        roomId: String
    ): Result<LiveRoomDetail> {
        return runCatching {
            val site = getSite(siteId)
            site.getRoomDetail(roomId)
        }
    }

    override suspend fun getPlayQualities(
        siteId: String,
        detail: LiveRoomDetail
    ): Result<List<LivePlayQuality>> {
        return runCatching {
            val site = getSite(siteId)
            site.getPlayQualities(detail)
        }
    }

    override suspend fun getPlayUrls(
        siteId: String,
        detail: LiveRoomDetail,
        quality: LivePlayQuality
    ): Result<LivePlayUrl> {
        return runCatching {
            val site = getSite(siteId)
            site.getPlayUrls(detail, quality)
        }
    }

    override suspend fun getLiveStatus(
        siteId: String,
        roomId: String
    ): Result<Boolean> {
        return runCatching {
            val site = getSite(siteId)
            site.getLiveStatus(roomId)
        }
    }

    /**
     * Get LiveSite implementation by ID
     */
    private fun getSite(siteId: String): LiveSite {
        return sites[siteId] ?: throw IllegalArgumentException("Unknown site: $siteId")
    }
}