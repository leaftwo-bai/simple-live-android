package com.xycz.simplelive.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xycz.simplelive.core.model.LiveRoomItem
import com.xycz.simplelive.data.preferences.PreferencesManager
import com.xycz.simplelive.domain.usecase.GetAllSitesUseCase
import com.xycz.simplelive.domain.usecase.GetRecommendRoomsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Home screen
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    // private val preferencesManager: PreferencesManager,
    private val getAllSitesUseCase: GetAllSitesUseCase,
    private val getRecommendRoomsUseCase: GetRecommendRoomsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Available sites (platforms)
    private val _sites = MutableStateFlow<List<SiteInfo>>(emptyList())
    val sites: StateFlow<List<SiteInfo>> = _sites.asStateFlow()

    init {
        loadSites()
        // Don't load site order on init - can cause ClassCastException
        // loadSiteOrder()
    }

    /**
     * Load available sites
     */
    private fun loadSites() {
        viewModelScope.launch {
            val siteModels = getAllSitesUseCase()
            _sites.value = siteModels.map { SiteInfo(it.id, it.name) }
            // Load rooms for first site
            if (_sites.value.isNotEmpty()) {
                loadRooms()
            }
        }
    }

    /**
     * Load site order from preferences
     */
    private fun loadSiteOrder() {
        viewModelScope.launch {
            preferencesManager.siteSort.collectLatest { sortedIds ->
                val currentSites = _sites.value
                if (currentSites.isEmpty()) return@collectLatest

                val sortedSites = sortedIds.mapNotNull { id ->
                    currentSites.find { it.id == id }
                }
                if (sortedSites.isNotEmpty()) {
                    _sites.value = sortedSites
                }
            }
        }
    }

    /**
     * Select a site/platform
     */
    fun selectSite(index: Int) {
        _uiState.update { it.copy(selectedSiteIndex = index) }
        loadRooms()
    }

    /**
     * Load recommended rooms for current site
     */
    private fun loadRooms() {
        val currentSites = _sites.value
        if (currentSites.isEmpty()) return

        val selectedIndex = _uiState.value.selectedSiteIndex
        if (selectedIndex >= currentSites.size) return

        val currentSite = currentSites[selectedIndex]

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getRecommendRoomsUseCase(currentSite.id, page = 1)
                .onSuccess { result ->
                    _uiState.update {
                        it.copy(
                            rooms = result.items,
                            isLoading = false,
                            hasMore = result.hasMore
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            error = error.message ?: "Unknown error",
                            isLoading = false
                        )
                    }
                }
        }
    }

    /**
     * Refresh current rooms
     */
    fun refresh() {
        loadRooms()
    }
}

/**
 * UI state for Home screen
 */
data class HomeUiState(
    val selectedSiteIndex: Int = 0,
    val rooms: List<LiveRoomItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasMore: Boolean = false
)

/**
 * Site information
 */
data class SiteInfo(
    val id: String,
    val name: String
)