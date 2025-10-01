package com.xycz.simplelive.core.danmaku.bilibili

import com.xycz.simplelive.core.danmaku.LiveDanmaku
import com.xycz.simplelive.core.model.*
import com.xycz.simplelive.core.network.WebSocketClient
import com.xycz.simplelive.core.network.WebSocketMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okio.ByteString.Companion.toByteString
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.zip.Inflater

/**
 * BiliBili danmaku client implementation
 * Handles WebSocket connection and message parsing
 */
class BiliBiliDanmaku : LiveDanmaku {

    private var webSocketClient: WebSocketClient? = null
    private var heartbeatJob: Job? = null
    private val heartbeatScope = CoroutineScope(Dispatchers.IO)

    companion object {
        // Protocol operation codes
        private const val OP_HANDSHAKE = 7
        private const val OP_HANDSHAKE_REPLY = 8
        private const val OP_HEARTBEAT = 2
        private const val OP_HEARTBEAT_REPLY = 3
        private const val OP_MESSAGE = 5

        // Protocol version
        private const val PROTOCOL_VERSION_NORMAL = 0
        private const val PROTOCOL_VERSION_HEARTBEAT = 1
        private const val PROTOCOL_VERSION_DEFLATE = 2

        // Packet header length
        private const val HEADER_LENGTH = 16
    }

    override suspend fun start(detail: LiveRoomDetail): Flow<LiveMessage> = flow {
        // Get danmaku server info from detail.danmakuData
        val roomId = detail.data ?: detail.roomId

        // Default BiliBili danmaku server
        val wsUrl = "wss://broadcastlv.chat.bilibili.com/sub"

        webSocketClient = WebSocketClient()

        webSocketClient?.connect(wsUrl)?.collect { message ->
            when (message) {
                is WebSocketMessage.Connected -> {
                    // Send handshake
                    sendHandshake(roomId)
                    // Start heartbeat
                    startHeartbeat()
                }

                is WebSocketMessage.BinaryMessage -> {
                    // Parse danmaku messages
                    parseDanmakuPacket(message.bytes).forEach { danmakuMessage ->
                        emit(danmakuMessage)
                    }
                }

                is WebSocketMessage.Closed -> {
                    stopHeartbeat()
                }

                is WebSocketMessage.Error -> {
                    stopHeartbeat()
                }

                else -> {}
            }
        }
    }

    override suspend fun stop() {
        stopHeartbeat()
        webSocketClient?.disconnect()
        webSocketClient = null
    }

    override suspend fun sendMessage(message: String): Result<Unit> {
        // BiliBili doesn't support sending messages via WebSocket
        return Result.failure(UnsupportedOperationException("BiliBili doesn't support sending danmaku via WebSocket"))
    }

    /**
     * Send handshake packet
     */
    private fun sendHandshake(roomId: String) {
        val body = """{"roomid":$roomId,"uid":0,"protover":2}""".toByteArray()
        val packet = createPacket(OP_HANDSHAKE, body)
        webSocketClient?.sendBinary(packet)
    }

    /**
     * Start heartbeat timer
     */
    private fun startHeartbeat() {
        heartbeatJob = heartbeatScope.launch {
            while (true) {
                delay(30000) // Send heartbeat every 30 seconds
                sendHeartbeat()
            }
        }
    }

    /**
     * Stop heartbeat timer
     */
    private fun stopHeartbeat() {
        heartbeatJob?.cancel()
        heartbeatJob = null
    }

    /**
     * Send heartbeat packet
     */
    private fun sendHeartbeat() {
        val packet = createPacket(OP_HEARTBEAT, "[object Object]".toByteArray())
        webSocketClient?.sendBinary(packet)
    }

    /**
     * Create BiliBili protocol packet
     */
    private fun createPacket(operation: Int, body: ByteArray): ByteArray {
        val packetLength = HEADER_LENGTH + body.size
        val buffer = ByteBuffer.allocate(packetLength).apply {
            order(ByteOrder.BIG_ENDIAN)
            putInt(packetLength)           // Packet length
            putShort(HEADER_LENGTH.toShort()) // Header length
            putShort(PROTOCOL_VERSION_DEFLATE.toShort()) // Protocol version
            putInt(operation)              // Operation
            putInt(1)                      // Sequence
            put(body)                      // Body
        }
        return buffer.array()
    }

    /**
     * Parse danmaku packet
     */
    private fun parseDanmakuPacket(data: ByteArray): List<LiveMessage> {
        val messages = mutableListOf<LiveMessage>()
        var offset = 0

        while (offset < data.size) {
            if (offset + HEADER_LENGTH > data.size) break

            val buffer = ByteBuffer.wrap(data, offset, HEADER_LENGTH).apply {
                order(ByteOrder.BIG_ENDIAN)
            }

            val packetLength = buffer.getInt()
            val headerLength = buffer.getShort().toInt()
            val protocolVersion = buffer.getShort().toInt()
            val operation = buffer.getInt()
            // val sequence = buffer.getInt()

            if (offset + packetLength > data.size) break

            val bodyOffset = offset + headerLength
            val bodyLength = packetLength - headerLength

            when (operation) {
                OP_HANDSHAKE_REPLY -> {
                    // Handshake successful
                }

                OP_HEARTBEAT_REPLY -> {
                    // Heartbeat reply contains online count
                    if (bodyLength >= 4) {
                        val online = ByteBuffer.wrap(data, bodyOffset, 4).apply {
                            order(ByteOrder.BIG_ENDIAN)
                        }.getInt()

                        messages.add(
                            LiveMessage(
                                type = LiveMessageType.ONLINE,
                                userName = "",
                                message = "",
                                data = online.toString(),
                                color = LiveMessageColor.WHITE
                            )
                        )
                    }
                }

                OP_MESSAGE -> {
                    // Parse message body
                    val bodyData = data.copyOfRange(bodyOffset, bodyOffset + bodyLength)

                    // Decompress if needed
                    val decompressed = if (protocolVersion == PROTOCOL_VERSION_DEFLATE) {
                        decompressData(bodyData)
                    } else {
                        bodyData
                    }

                    // Parse JSON messages recursively
                    messages.addAll(parseDanmakuPacket(decompressed))
                }
            }

            offset += packetLength
        }

        return messages
    }

    /**
     * Decompress zlib data
     */
    private fun decompressData(data: ByteArray): ByteArray {
        val inflater = Inflater()
        inflater.setInput(data)
        val outputStream = java.io.ByteArrayOutputStream(data.size)
        val buffer = ByteArray(1024)

        try {
            while (!inflater.finished()) {
                val count = inflater.inflate(buffer)
                outputStream.write(buffer, 0, count)
            }
        } finally {
            inflater.end()
        }

        return outputStream.toByteArray()
    }
}