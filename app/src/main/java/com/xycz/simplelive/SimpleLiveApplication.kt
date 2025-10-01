package com.xycz.simplelive

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Simple Live Application class
 * Entry point for the app with Hilt initialization
 */
@HiltAndroidApp
class SimpleLiveApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize app-level components here if needed
    }
}