package com.xycz.simplelive

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.xycz.simplelive.data.preferences.PreferencesManager
import com.xycz.simplelive.ui.navigation.SimpleLiveNavGraph
import com.xycz.simplelive.ui.theme.SimpleLiveTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Main Activity
 * Entry point for the Compose UI
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display
        enableEdgeToEdge()

        // Allow content to draw behind system bars
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val themeMode by preferencesManager.themeMode.collectAsState(initial = 0)
            val isDynamicColor by preferencesManager.isDynamicColor.collectAsState(initial = true)

            SimpleLiveTheme(
                darkTheme = when (themeMode) {
                    1 -> false // Light
                    2 -> true  // Dark
                    else -> false // System (will be handled in theme)
                },
                dynamicColor = isDynamicColor
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SimpleLiveNavGraph()
                }
            }
        }
    }
}