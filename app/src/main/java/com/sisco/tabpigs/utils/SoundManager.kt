package com.sisco.tabpigs.utils

import android.media.AudioAttributes
import android.media.SoundPool
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import com.sisco.tabpigs.R

class SoundManager(
    private val soundPool: SoundPool?,
    private val sfxClickId: Int
) {
    fun playClick() {
        soundPool?.play(sfxClickId, 0.5f, 0.5f, 1, 0, 1.0f)
    }
}

@Composable
fun rememberSoundManager(): SoundManager {
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current

    if (isPreview) {
        return remember { SoundManager(soundPool = null, sfxClickId = 0) }
    }

    val soundPool = remember {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        SoundPool.Builder()
            .setMaxStreams(3)
            .setAudioAttributes(audioAttributes)
            .build()
    }

    val sfxClickId = remember {
        soundPool.load(context, R.raw.sfx_click, 1)
    }

    DisposableEffect(Unit) {
        onDispose {
            soundPool.release()
        }
    }

    return remember(soundPool, sfxClickId) {
        SoundManager(soundPool, sfxClickId)
    }
}