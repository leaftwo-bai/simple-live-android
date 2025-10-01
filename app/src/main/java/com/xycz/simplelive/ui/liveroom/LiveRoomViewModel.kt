package com.xycz.simplelive.ui.liveroom

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xycz.simplelive.core.model.*
import com.xycz.simplelive.domain.repository.LiveRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for LiveRoom screen
 */
@HiltViewModel
class LiveRoomViewModel @Inject constructor(
    private val liveRepository: LiveRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val siteId: String = savedStateHandle["siteId"] ?: ""
    private val roomId: String = savedStateHandle["roomId"] ?: ""

    private val _uiState = MutableStateFlow(LiveRoomUiState())
    val uiState: StateFlow<LiveRoomUiState> = _uiState.asStateFlow()

    private val _messages = MutableStateFlow<List<LiveMessage>>(emptyList())
    val messages: StateFlow<List<LiveMessage>> = _messages.asStateFlow()

    init {
        loadRoomDetail()
    }

    /**
     * Load room detail and initial data
     */
    private fun loadRoomDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            liveRepository.getRoomDetail(siteId, roomId)
                .onSuccess { detail ->
                    _uiState.update {
                        it.copy(
                            roomDetail = detail,
                            isLoading = false,
                            isLive = detail.status
                        )
                    }

                    // Load qualities if live
                    if (detail.status) {
                        loadPlayQualities(detail)
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            error = error.message ?: "Failed to load room",
                            isLoading = false
                        )
                    }
                }
        }
    }

    /**
     * Load available play qualities
     */
    private fun loadPlayQualities(detail: LiveRoomDetail) {
        viewModelScope.launch {
            liveRepository.getPlayQualities(siteId, detail)
                .onSuccess { qualities ->
                    _uiState.update {
                        it.copy(
                            qualities = qualities,
                            selectedQuality = qualities.firstOrNull()
                        )
                    }

                    // Load play URLs for default quality
                    qualities.firstOrNull()?.let { quality ->
                        loadPlayUrls(detail, quality)
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(error = "Failed to load qualities: ${error.message}")
                    }
                }
        }
    }

    /**
     * Load play URLs for selected quality
     */
    private fun loadPlayUrls(detail: LiveRoomDetail, quality: LivePlayQuality) {
        viewModelScope.launch {
            liveRepository.getPlayUrls(siteId, detail, quality)
                .onSuccess { playUrl ->
                    _uiState.update {
                        it.copy(
                            playUrl = playUrl.urls.firstOrNull() ?: "",
                            playHeaders = playUrl.headers,
                            selectedQuality = quality
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(error = "Failed to load play URL: ${error.message}")
                    }
                }
        }
    }

    /**
     * Select a different quality
     */
    fun selectQuality(quality: LivePlayQuality) {
        val detail = _uiState.value.roomDetail ?: return
        loadPlayUrls(detail, quality)
    }

    /**
     * Toggle danmaku visibility
     */
    fun toggleDanmaku() {
        _uiState.update { it.copy(showDanmaku = !it.showDanmaku) }
    }

    /**
     * Toggle player controls visibility
     */
    fun toggleControls() {
        _uiState.update { it.copy(showControls = !it.showControls) }
    }

    /**
     * Refresh room status
     */
    fun refresh() {
        loadRoomDetail()
    }

    /**
     * Add a danmaku message (for testing)
     */
    fun addDanmakuMessage(message: LiveMessage) {
        _messages.update { messages ->
            (messages + message).takeLast(100) // Keep last 100 messages
        }
    }
}

/**
 * UI state for LiveRoom screen
 */
data class LiveRoomUiState(
    val roomDetail: LiveRoomDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLive: Boolean = false,
    val qualities: List<LivePlayQuality> = emptyList(),
    val selectedQuality: LivePlayQuality? = null,
    val playUrl: String = "",
    val playHeaders: Map<String, String>? = null,
    val showDanmaku: Boolean = true,
    val showControls: Boolean = true,
    val isPlaying: Boolean = false
)