package com.sisco.tabpigs.utils

import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.sisco.tabpigs.R

@Composable
fun BackgroundMusicManager(isGameRunning: Boolean, isMuted: Boolean) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val mediaPlayer = remember {
        MediaPlayer.create(context, R.raw.sonican_bright_sun).apply {
            isLooping = true
        }
    }

    LaunchedEffect(isGameRunning, isMuted) {
        if (isMuted) {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            }
        } else {
            val volume = if (isGameRunning) 0.1f else 0.3f
            mediaPlayer.setVolume(volume, volume)

            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    if (!mediaPlayer.isPlaying) {
                        mediaPlayer.start()
                    }
                }
                Lifecycle.Event.ON_PAUSE -> {
                    if (mediaPlayer.isPlaying) {
                        mediaPlayer.pause()
                    }
                }

                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
            mediaPlayer.release()
        }
    }
}