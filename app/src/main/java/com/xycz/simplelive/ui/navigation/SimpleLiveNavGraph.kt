package com.xycz.simplelive.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.xycz.simplelive.ui.home.HomeScreen

/**
 * Main navigation graph for Simple Live
 */
@Composable
fun SimpleLiveNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Home screen (main entry point)
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        // Live room screen
        composable(
            route = "${Screen.LiveRoom.route}/{siteId}/{roomId}",
            arguments = listOf(
                navArgument("siteId") { type = NavType.StringType },
                navArgument("roomId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val siteId = backStackEntry.arguments?.getString("siteId") ?: ""
            val roomId = backStackEntry.arguments?.getString("roomId") ?: ""

            com.xycz.simplelive.ui.liveroom.LiveRoomScreen(
                siteId = siteId,
                roomId = roomId,
                navController = navController
            )
        }

        // Search screen
        composable(Screen.Search.route) {
            // SearchScreen will be implemented later
            // SearchScreen(navController = navController)
        }

        // Category screen
        composable(Screen.Category.route) {
            // CategoryScreen will be implemented later
            // CategoryScreen(navController = navController)
        }

        // Follow screen
        composable(Screen.Follow.route) {
            // FollowScreen will be implemented later
            // FollowScreen(navController = navController)
        }

        // Mine/Profile screen
        composable(Screen.Mine.route) {
            // MineScreen will be implemented later
            // MineScreen(navController = navController)
        }

        // History screen
        composable(Screen.History.route) {
            // HistoryScreen will be implemented later
            // HistoryScreen(navController = navController)
        }

        // Settings screens
        composable(Screen.Settings.route) {
            // SettingsScreen will be implemented later
            // SettingsScreen(navController = navController)
        }

        // Additional screens can be added here
    }
}