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
    private val sfxClickId: Map<ItemType, Int>
) {
    fun playClick(type: ItemType) {
        val soundId = sfxClickId[type] ?: return
        soundPool?.play(soundId, 0.8f, 0.8f, 1, 0, 1.0f)
    }
}

@Composable
fun rememberSoundManager(): SoundManager {
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current

    if (isPreview) {
        return remember { SoundManager(soundPool = null, sfxClickId = emptyMap()) }
    }

    val soundPool = remember {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()
    }

    val sfxClickId = remember {
        mapOf(
            ItemType.NORMAL to soundPool.load(context, R.raw.sfx_click, 1),
            ItemType.GOLDEN to soundPool.load(context, R.raw.sfx_golden, 1),
            ItemType.BOMB to soundPool.load(context, R.raw.sfx_bomb, 1)
        )
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