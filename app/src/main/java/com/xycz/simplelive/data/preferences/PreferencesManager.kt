package com.xycz.simplelive.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Preferences manager using DataStore
 * Replaces Flutter's Hive LocalStorageService
 */
@Singleton
class PreferencesManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    // Theme settings
    val themeMode: Flow<Int> = dataStore.data.map { prefs ->
        prefs[THEME_MODE] ?: 0 // 0 = System, 1 = Light, 2 = Dark
    }

    val isDynamicColor: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[DYNAMIC_COLOR] ?: true
    }

    val styleColor: Flow<Int> = dataStore.data.map { prefs ->
        prefs[STYLE_COLOR] ?: 0xFF6200EE.toInt()
    }

    // Danmaku settings
    val enableDanmaku: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[ENABLE_DANMAKU] ?: true
    }

    val danmakuArea: Flow<Float> = dataStore.data.map { prefs ->
        prefs[DANMAKU_AREA] ?: 0.5f
    }

    val danmakuSpeed: Flow<Int> = dataStore.data.map { prefs ->
        prefs[DANMAKU_SPEED] ?: 8
    }

    val danmakuFontSize: Flow<Float> = dataStore.data.map { prefs ->
        prefs[DANMAKU_FONT_SIZE] ?: 16f
    }

    val danmakuFontBorder: Flow<Float> = dataStore.data.map { prefs ->
        prefs[DANMAKU_FONT_BORDER] ?: 0.5f
    }

    val danmakuOpacity: Flow<Float> = dataStore.data.map { prefs ->
        prefs[DANMAKU_OPACITY] ?: 1.0f
    }

    // Player settings
    val preferResolution: Flow<String> = dataStore.data.map { prefs ->
        prefs[PREFER_RESOLUTION] ?: "原画"
    }

    val preferPlatform: Flow<String> = dataStore.data.map { prefs ->
        prefs[PREFER_PLATFORM] ?: "web"
    }

    val autoFullScreen: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[AUTO_FULLSCREEN] ?: false
    }

    // Site settings
    val siteSort: Flow<List<String>> = dataStore.data.map { prefs ->
        val sortString = prefs[SITE_SORT] ?: "bilibili,douyu,huya,douyin"
        sortString.split(",")
    }

    // Auto exit settings
    val autoExitEnable: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[AUTO_EXIT_ENABLE] ?: false
    }

    val autoExitDuration: Flow<Int> = dataStore.data.map { prefs ->
        prefs[AUTO_EXIT_DURATION] ?: 60
    }

    // Follow settings
    val followRefreshInterval: Flow<Int> = dataStore.data.map { prefs ->
        prefs[FOLLOW_REFRESH_INTERVAL] ?: 60
    }

    // Update functions
    suspend fun setThemeMode(mode: Int) {
        dataStore.edit { prefs -> prefs[THEME_MODE] = mode }
    }

    suspend fun setDynamicColor(enabled: Boolean) {
        dataStore.edit { prefs -> prefs[DYNAMIC_COLOR] = enabled }
    }

    suspend fun setStyleColor(color: Int) {
        dataStore.edit { prefs -> prefs[STYLE_COLOR] = color }
    }

    suspend fun setEnableDanmaku(enabled: Boolean) {
        dataStore.edit { prefs -> prefs[ENABLE_DANMAKU] = enabled }
    }

    suspend fun setDanmakuArea(area: Float) {
        dataStore.edit { prefs -> prefs[DANMAKU_AREA] = area }
    }

    suspend fun setDanmakuSpeed(speed: Int) {
        dataStore.edit { prefs -> prefs[DANMAKU_SPEED] = speed }
    }

    suspend fun setDanmakuFontSize(size: Float) {
        dataStore.edit { prefs -> prefs[DANMAKU_FONT_SIZE] = size }
    }

    suspend fun setDanmakuFontBorder(border: Float) {
        dataStore.edit { prefs -> prefs[DANMAKU_FONT_BORDER] = border }
    }

    suspend fun setDanmakuOpacity(opacity: Float) {
        dataStore.edit { prefs -> prefs[DANMAKU_OPACITY] = opacity }
    }

    suspend fun setPreferResolution(resolution: String) {
        dataStore.edit { prefs -> prefs[PREFER_RESOLUTION] = resolution }
    }

    suspend fun setPreferPlatform(platform: String) {
        dataStore.edit { prefs -> prefs[PREFER_PLATFORM] = platform }
    }

    suspend fun setAutoFullScreen(enabled: Boolean) {
        dataStore.edit { prefs -> prefs[AUTO_FULLSCREEN] = enabled }
    }

    suspend fun setSiteSort(sites: List<String>) {
        dataStore.edit { prefs -> prefs[SITE_SORT] = sites.joinToString(",") }
    }

    suspend fun setAutoExitEnable(enabled: Boolean) {
        dataStore.edit { prefs -> prefs[AUTO_EXIT_ENABLE] = enabled }
    }

    suspend fun setAutoExitDuration(duration: Int) {
        dataStore.edit { prefs -> prefs[AUTO_EXIT_DURATION] = duration }
    }

    suspend fun setFollowRefreshInterval(interval: Int) {
        dataStore.edit { prefs -> prefs[FOLLOW_REFRESH_INTERVAL] = interval }
    }

    companion object {
        // Theme keys
        private val THEME_MODE = intPreferencesKey("theme_mode")
        private val DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color")
        private val STYLE_COLOR = intPreferencesKey("style_color")

        // Danmaku keys
        private val ENABLE_DANMAKU = booleanPreferencesKey("enable_danmaku")
        private val DANMAKU_AREA = floatPreferencesKey("danmaku_area")
        private val DANMAKU_SPEED = intPreferencesKey("danmaku_speed")
        private val DANMAKU_FONT_SIZE = floatPreferencesKey("danmaku_font_size")
        private val DANMAKU_FONT_BORDER = floatPreferencesKey("danmaku_font_border")
        private val DANMAKU_OPACITY = floatPreferencesKey("danmaku_opacity")

        // Player keys
        private val PREFER_RESOLUTION = stringPreferencesKey("prefer_resolution")
        private val PREFER_PLATFORM = stringPreferencesKey("prefer_platform")
        private val AUTO_FULLSCREEN = booleanPreferencesKey("auto_fullscreen")

        // Site keys
        private val SITE_SORT = stringPreferencesKey("site_sort")

        // Auto exit keys
        private val AUTO_EXIT_ENABLE = booleanPreferencesKey("auto_exit_enable")
        private val AUTO_EXIT_DURATION = intPreferencesKey("auto_exit_duration")

        // Follow keys
        private val FOLLOW_REFRESH_INTERVAL = intPreferencesKey("follow_refresh_interval")
    }
}