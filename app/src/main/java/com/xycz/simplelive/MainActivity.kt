package com.xycz.simplelive

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.xycz.simplelive.ui.navigation.SimpleLiveNavGraph
import com.xycz.simplelive.ui.theme.SimpleLiveTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main Activity
 * Entry point for the Compose UI
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display
        enableEdgeToEdge()

        // Allow content to draw behind system bars
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            // Use default theme settings (ProGuard rules now fixed)
            SimpleLiveTheme(
                darkTheme = false,
                dynamicColor = true
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