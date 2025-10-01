package com.xycz.simplelive.core.platform.bilibili

import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * BiliBili API service interface
 */
interface BiliBiliApiService {

    /**
     * Get all categories/areas
     */
    @GET("room/v1/Area/getList")
    suspend fun getCategories(
        @QueryMap params: Map<String, String>
    ): BiliBiliResponse<List<BiliBiliCategoryData>>

    /**
     * Get rooms in a category
     */
    @GET("xlive/web-interface/v1/second/getList")
    suspend fun getCategoryRooms(
        @QueryMap params: Map<String, String>
    ): BiliBiliResponse<BiliBiliRoomListData>

    /**
     * Get recommended rooms
     */
    @GET("xlive/web-interface/v1/second/getListByArea")
    suspend fun getRecommendRooms(
        @QueryMap params: Map<String, String>
    ): BiliBiliResponse<BiliBiliRoomListData>

    /**
     * Get room info
     */
    @GET("room/v1/Room/get_info")
    suspend fun getRoomInfo(
        @QueryMap params: Map<String, String>
    ): BiliBiliResponse<BiliBiliRoomInfoData>

    /**
     * Get room play info
     */
    @GET("xlive/web-room/v2/index/getRoomPlayInfo")
    suspend fun getRoomPlayInfo(
        @QueryMap params: Map<String, String>
    ): BiliBiliResponse<BiliBiliPlayInfoData>

    /**
     * Get danmaku info
     */
    @GET("xlive/web-room/v1/index/getDanmuInfo")
    suspend fun getDanmuInfo(
        @QueryMap params: Map<String, String>
    ): BiliBiliResponse<BiliBiliDanmuInfoData>

    /**
     * Search rooms
     */
    @GET("xlive/web-interface/v1/search/searchRoom")
    suspend fun searchRooms(
        @QueryMap params: Map<String, String>
    ): BiliBiliResponse<BiliBiliSearchRoomData>

    /**
     * Get buvid (device fingerprint)
     */
    @GET("x/frontend/finger/spi")
    suspend fun getBuvid(): BiliBiliResponse<BiliBiliBuvidData>

    companion object {
        const val BASE_URL = "https://api.live.bilibili.com/"
    }
}

/**
 * Generic BiliBili API response wrapper
 */
@kotlinx.serialization.Serializable
data class BiliBiliResponse<T>(
    val code: Int,
    val message: String? = null,
    val msg: String? = null,
    val data: T? = null
) {
    fun isSuccess() = code == 0

    fun getOrThrow(): T {
        if (code != 0) {
            throw BiliBiliApiException(code, message ?: msg ?: "Unknown error")
        }
        return data ?: throw BiliBiliApiException(-1, "Data is null")
    }
}

/**
 * BiliBili API exception
 */
class BiliBiliApiException(
    val code: Int,
    override val message: String
) : Exception(message)