package com.xycz.simplelive.ui.liveroom

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

/**
 * ExoPlayer video player composable
 */
@Composable
fun VideoPlayer(
    url: String,
    headers: Map<String, String>? = null,
    modifier: Modifier = Modifier,
    onPlayerReady: (Player) -> Unit = {}
) {
    val context = LocalContext.current

    val exoPlayer = remember(url) {
        if (url.isEmpty()) {
            null
        } else {
            ExoPlayer.Builder(context).build().apply {
                // Build media item with headers if provided
                val mediaItemBuilder = MediaItem.Builder().setUri(url)

                // TODO: Add headers support if needed
                // This requires implementing a custom DataSource.Factory

                setMediaItem(mediaItemBuilder.build())
                prepare()
                playWhenReady = true

                addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        when (playbackState) {
                            Player.STATE_READY -> onPlayerReady(this@apply)
                            Player.STATE_ENDED -> {
                                // Handle playback ended
                            }
                            else -> {}
                        }
                    }
                })
            }
        }
    }

    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer?.release()
        }
    }

    if (exoPlayer != null) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    useController = false // We'll use custom controls
                    setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
                }
            },
            modifier = modifier.fillMaxSize()
        )
    } else {
        // Placeholder when no URL
        androidx.compose.foundation.layout.Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black)
        )
    }
}