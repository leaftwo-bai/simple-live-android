package com.xycz.simplelive.ui.liveroom

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

/**
 * Live room screen - main viewing screen
 * Shows video player, danmaku, and room info
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveRoomScreen(
    siteId: String,
    roomId: String,
    navController: NavController,
    viewModel: LiveRoomViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val messages by viewModel.messages.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> {
                // Loading state
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }

            uiState.error != null -> {
                // Error state
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = uiState.error ?: "Unknown error",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.refresh() }) {
                            Text("Retry")
                        }
                    }
                }
            }

            !uiState.isLive -> {
                // Room is offline
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "This room is offline",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.roomDetail?.userName ?: "",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            else -> {
                // Video player
                VideoPlayer(
                    url = uiState.playUrl,
                    headers = uiState.playHeaders,
                    modifier = Modifier.fillMaxSize()
                )

                // Overlay controls and info
                LiveRoomOverlay(
                    uiState = uiState,
                    messages = messages,
                    onBack = { navController.popBackStack() },
                    onToggleDanmaku = { viewModel.toggleDanmaku() },
                    onSelectQuality = { quality -> viewModel.selectQuality(quality) }
                )
            }
        }
    }
}

/**
 * Overlay with controls and information
 */
@Composable
fun LiveRoomOverlay(
    uiState: LiveRoomUiState,
    messages: List<com.xycz.simplelive.core.model.LiveMessage>,
    onBack: () -> Unit,
    onToggleDanmaku: () -> Unit,
    onSelectQuality: (com.xycz.simplelive.core.model.LivePlayQuality) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Top bar with room info and controls
        TopBar(
            roomDetail = uiState.roomDetail,
            onBack = onBack,
            modifier = Modifier.align(Alignment.TopStart)
        )

        // Danmaku overlay (if enabled)
        if (uiState.showDanmaku && messages.isNotEmpty()) {
            DanmakuOverlay(
                messages = messages,
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }

        // Bottom controls
        BottomControls(
            qualities = uiState.qualities,
            selectedQuality = uiState.selectedQuality,
            showDanmaku = uiState.showDanmaku,
            onToggleDanmaku = onToggleDanmaku,
            onSelectQuality = onSelectQuality,
            modifier = Modifier.align(Alignment.BottomStart)
        )
    }
}

/**
 * Top bar with room info
 */
@Composable
fun TopBar(
    roomDetail: com.xycz.simplelive.core.model.LiveRoomDetail?,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.5f))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = roomDetail?.title ?: "",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1
            )
            Text(
                text = "${roomDetail?.userName ?: ""} · ${roomDetail?.online ?: 0} watching",
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )
        }

        IconButton(onClick = { /* TODO: Follow */ }) {
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = "Follow",
                tint = Color.White
            )
        }

        IconButton(onClick = { /* TODO: Share */ }) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share",
                tint = Color.White
            )
        }
    }
}

/**
 * Danmaku overlay (simplified version)
 */
@Composable
fun DanmakuOverlay(
    messages: List<com.xycz.simplelive.core.model.LiveMessage>,
    modifier: Modifier = Modifier
) {
    // TODO: Implement proper danmaku rendering with Canvas
    // For now, just show recent messages at the bottom
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        messages.takeLast(5).forEach { message ->
            Text(
                text = "${message.userName}: ${message.message}",
                color = Color.White,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.6f))
                    .padding(4.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

/**
 * Bottom controls for quality and danmaku toggle
 */
@Composable
fun BottomControls(
    qualities: List<com.xycz.simplelive.core.model.LivePlayQuality>,
    selectedQuality: com.xycz.simplelive.core.model.LivePlayQuality?,
    showDanmaku: Boolean,
    onToggleDanmaku: () -> Unit,
    onSelectQuality: (com.xycz.simplelive.core.model.LivePlayQuality) -> Unit,
    modifier: Modifier = Modifier
) {
    var showQualityMenu by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.5f))
            .padding(8.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Danmaku toggle
        IconButton(onClick = onToggleDanmaku) {
            Icon(
                imageVector = if (showDanmaku) Icons.Default.Chat else Icons.Default.ChatBubbleOutline,
                contentDescription = "Toggle Danmaku",
                tint = Color.White
            )
        }

        // Quality selector
        Box {
            TextButton(onClick = { showQualityMenu = true }) {
                Text(
                    text = selectedQuality?.quality ?: "Quality",
                    color = Color.White
                )
            }

            DropdownMenu(
                expanded = showQualityMenu,
                onDismissRequest = { showQualityMenu = false }
            ) {
                qualities.forEach { quality ->
                    DropdownMenuItem(
                        text = { Text(quality.quality) },
                        onClick = {
                            onSelectQuality(quality)
                            showQualityMenu = false
                        }
                    )
                }
            }
        }

        // Fullscreen toggle (placeholder)
        IconButton(onClick = { /* TODO: Fullscreen */ }) {
            Icon(
                imageVector = Icons.Default.Fullscreen,
                contentDescription = "Fullscreen",
                tint = Color.White
            )
        }
    }
}