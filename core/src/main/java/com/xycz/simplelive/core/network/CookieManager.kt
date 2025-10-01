package com.xycz.simplelive.core.network

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 * Simple in-memory cookie manager
 * Stores cookies per host
 */
class SimpleCookieJar : CookieJar {
    private val cookieStore = mutableMapOf<String, MutableList<Cookie>>()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        val host = url.host
        cookieStore.getOrPut(host) { mutableListOf() }.apply {
            // Remove expired cookies and duplicates
            removeAll { existing ->
                cookies.any { new -> new.name == existing.name && new.domain == existing.domain }
            }
            addAll(cookies.filter { !it.expiresAt.isBefore(System.currentTimeMillis()) })
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val host = url.host
        val now = System.currentTimeMillis()

        return cookieStore[host]?.filter { cookie ->
            !cookie.expiresAt.isBefore(now)
        } ?: emptyList()
    }

    /**
     * Clear all cookies
     */
    fun clear() {
        cookieStore.clear()
    }

    /**
     * Clear cookies for a specific host
     */
    fun clearForHost(host: String) {
        cookieStore.remove(host)
    }

    /**
     * Get all cookies as a map
     */
    fun getAllCookies(): Map<String, List<Cookie>> {
        return cookieStore.toMap()
    }

    private fun Long.isBefore(timestamp: Long): Boolean {
        return this < timestamp
    }
}