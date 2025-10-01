package com.xycz.simplelive.core.network

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit

/**
 * WebSocket client wrapper
 * Provides a Flow-based API for WebSocket communication
 */
class WebSocketClient(
    private val okHttpClient: OkHttpClient = HttpClient.createOkHttpClient()
) {
    private var webSocket: WebSocket? = null

    /**
     * Connect to WebSocket and return a Flow of messages
     */
    fun connect(
        url: String,
        headers: Map<String, String> = emptyMap()
    ): Flow<WebSocketMessage> = callbackFlow {
        val request = Request.Builder()
            .url(url)
            .apply {
                headers.forEach { (key, value) ->
                    addHeader(key, value)
                }
            }
            .build()

        webSocket = okHttpClient.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                trySend(WebSocketMessage.Connected)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                trySend(WebSocketMessage.TextMessage(text))
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                trySend(WebSocketMessage.BinaryMessage(bytes.toByteArray()))
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                trySend(WebSocketMessage.Closing(code, reason))
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                trySend(WebSocketMessage.Closed(code, reason))
                close()
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                trySend(WebSocketMessage.Error(t))
                close(t)
            }
        })

        awaitClose {
            disconnect()
        }
    }

    /**
     * Send text message
     */
    fun sendText(text: String): Boolean {
        return webSocket?.send(text) ?: false
    }

    /**
     * Send binary message
     */
    fun sendBinary(bytes: ByteArray): Boolean {
        return webSocket?.send(ByteString.of(*bytes)) ?: false
    }

    /**
     * Disconnect WebSocket
     */
    fun disconnect(code: Int = 1000, reason: String = "Normal closure") {
        webSocket?.close(code, reason)
        webSocket = null
    }
}

/**
 * WebSocket message types
 */
sealed class WebSocketMessage {
    object Connected : WebSocketMessage()
    data class TextMessage(val text: String) : WebSocketMessage()
    data class BinaryMessage(val bytes: ByteArray) : WebSocketMessage() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as BinaryMessage
            return bytes.contentEquals(other.bytes)
        }

        override fun hashCode(): Int {
            return bytes.contentHashCode()
        }
    }
    data class Closing(val code: Int, val reason: String) : WebSocketMessage()
    data class Closed(val code: Int, val reason: String) : WebSocketMessage()
    data class Error(val throwable: Throwable) : WebSocketMessage()
}