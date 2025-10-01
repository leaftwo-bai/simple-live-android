package com.xycz.simplelive.ui.navigation

/**
 * Screen definitions for navigation
 */
sealed class Screen(val route: String) {
    // Main screens
    object Home : Screen("home")
    object Category : Screen("category")
    object Follow : Screen("follow")
    object Mine : Screen("mine")

    // Detail screens
    object LiveRoom : Screen("live_room")
    object Search : Screen("search")
    object History : Screen("history")
    object Settings : Screen("settings")
    object CategoryDetail : Screen("category_detail")
    object Account : Screen("account")

    // Settings screens
    object DanmakuSettings : Screen("danmaku_settings")
    object PlaySettings : Screen("play_settings")
    object AppStyleSettings : Screen("app_style_settings")
    object FollowSettings : Screen("follow_settings")
    object AutoExitSettings : Screen("auto_exit_settings")
    object OtherSettings : Screen("other_settings")

    // Sync screens
    object Sync : Screen("sync")
    object LocalSync : Screen("local_sync")
    object RemoteSync : Screen("remote_sync")

    companion object {
        /**
         * Build route with parameters
         */
        fun LiveRoom.buildRoute(siteId: String, roomId: String): String {
            return "${route}/$siteId/$roomId"
        }

        fun CategoryDetail.buildRoute(siteId: String, categoryId: String): String {
            return "${route}/$siteId/$categoryId"
        }
    }
}