package com.xycz.simplelive.core.model

import kotlinx.serialization.Serializable

/**
 * Live play URL model
 * Contains streaming URLs and headers
 */
@Serializable
data class LivePlayUrl(
    /** List of stream URLs (multiple lines/CDNs) */
    val urls: List<String>,

    /** Custom headers for HTTP requests */
    val headers: Map<String, String>? = null
)