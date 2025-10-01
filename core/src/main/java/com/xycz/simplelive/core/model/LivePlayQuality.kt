package com.xycz.simplelive.core.model

import kotlinx.serialization.Serializable

/**
 * Live play quality model
 * Represents a quality option for streaming
 */
@Serializable
data class LivePlayQuality(
    /** Quality name (e.g., "1080P", "720P", "原画") */
    val quality: String,

    /** Platform-specific quality data */
    val data: String? = null,

    /** Sort order for display */
    val sort: Int = 0
)