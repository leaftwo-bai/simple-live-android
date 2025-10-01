package com.xycz.simplelive.ui.extensions

import androidx.compose.ui.graphics.Color
import com.xycz.simplelive.core.model.LiveMessageColor

/**
 * Extension functions for color conversions
 */

/**
 * Convert LiveMessageColor to Compose Color
 */
fun LiveMessageColor.toComposeColor(): Color = Color(r, g, b)

/**
 * Convert Compose Color to LiveMessageColor
 */
fun Color.toLiveMessageColor(): LiveMessageColor {
    val argb = this.value.toInt()
    return LiveMessageColor(
        r = (argb shr 16) and 0xFF,
        g = (argb shr 8) and 0xFF,
        b = argb and 0xFF
    )
}
