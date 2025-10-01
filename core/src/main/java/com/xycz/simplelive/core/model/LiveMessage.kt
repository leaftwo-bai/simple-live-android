package com.xycz.simplelive.core.model

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable

/**
 * Live message type enum
 */
enum class LiveMessageType {
    /** Chat message */
    CHAT,

    /** Gift message (not currently supported) */
    GIFT,

    /** Online viewer count update */
    ONLINE,

    /** Super chat / highlighted message */
    SUPER_CHAT
}

/**
 * Live message model
 * Represents a danmaku or chat message
 */
@Serializable
data class LiveMessage(
    /** Message type */
    val type: LiveMessageType,

    /** Username of the sender */
    val userName: String,

    /** Message content */
    val message: String,

    /** Additional data (e.g., viewer count for ONLINE type) */
    val data: String? = null,

    /** Message color in RGB format */
    val color: LiveMessageColor
)

/**
 * Live message color model
 */
@Serializable
data class LiveMessageColor(
    val r: Int,
    val g: Int,
    val b: Int
) {
    companion object {
        val WHITE = LiveMessageColor(255, 255, 255)

        /**
         * Convert integer color to LiveMessageColor
         * Supports 4, 6, and 8 digit hex colors
         */
        fun fromInt(intColor: Int): LiveMessageColor {
            val hex = intColor.toString(16).padStart(8, '0')

            return when (hex.length) {
                4 -> {
                    val paddedHex = "00$hex"
                    LiveMessageColor(
                        r = paddedHex.substring(0, 2).toInt(16),
                        g = paddedHex.substring(2, 4).toInt(16),
                        b = paddedHex.substring(4, 6).toInt(16)
                    )
                }
                6 -> LiveMessageColor(
                    r = hex.substring(0, 2).toInt(16),
                    g = hex.substring(2, 4).toInt(16),
                    b = hex.substring(4, 6).toInt(16)
                )
                8 -> LiveMessageColor(
                    r = hex.substring(2, 4).toInt(16),
                    g = hex.substring(4, 6).toInt(16),
                    b = hex.substring(6, 8).toInt(16)
                )
                else -> WHITE
            }
        }
    }

    /** Convert to Compose Color */
    fun toComposeColor(): Color = Color(r, g, b)

    /** Convert to hex string */
    fun toHexString(): String = "#${r.toString(16).padStart(2, '0')}${g.toString(16).padStart(2, '0')}${b.toString(16).padStart(2, '0')}"
}

/**
 * Super chat message model
 * Represents a highlighted paid message
 */
@Serializable
data class LiveSuperChatMessage(
    /** Username of the sender */
    val userName: String,

    /** User avatar URL */
    val face: String,

    /** Message content */
    val message: String,

    /** Price in cents/currency units */
    val price: Int,

    /** Message start time in milliseconds */
    val startTime: Long,

    /** Message end time in milliseconds */
    val endTime: Long,

    /** Background color hex string */
    val backgroundColor: String,

    /** Bottom background color hex string */
    val backgroundBottomColor: String
)