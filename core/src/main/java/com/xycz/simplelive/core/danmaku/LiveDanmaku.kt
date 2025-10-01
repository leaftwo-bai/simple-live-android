package com.xycz.simplelive.core.danmaku

import com.xycz.simplelive.core.model.LiveMessage
import com.xycz.simplelive.core.model.LiveRoomDetail
import kotlinx.coroutines.flow.Flow

/**
 * Live danmaku interface
 * Abstract interface for danmaku/chat connections
 */
interface LiveDanmaku {
    /**
     * Start danmaku connection
     * @param detail Room detail containing connection info
     * @return Flow of live messages
     */
    suspend fun start(detail: LiveRoomDetail): Flow<LiveMessage>

    /**
     * Stop danmaku connection
     */
    suspend fun stop()

    /**
     * Send a chat message (if supported)
     * @param message Message content
     */
    suspend fun sendMessage(message: String): Result<Unit>
}