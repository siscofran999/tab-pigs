package com.sisco.tabpigs.ui.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sisco.tabpigs.R
import com.sisco.tabpigs.findActivity
import com.sisco.tabpigs.ui.components.GameStatusDialog
import com.sisco.tabpigs.utils.SoundManager
import com.sisco.tabpigs.utils.rememberInterstitialAd
import com.sisco.tabpigs.utils.rememberSoundManager
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun PlayScreen(
    onNavigateBack: () -> Unit
) {
    var score by remember { mutableIntStateOf(0) }
    var level by remember { mutableIntStateOf(1) }
    var targetScore by remember { mutableIntStateOf(20) }

    var isGameRunning by remember { mutableStateOf(true) }
    var activeMoleIndex by remember { mutableIntStateOf(-1) }
    var timeLeftProgress by remember { mutableFloatStateOf(0f) }

    var showDialog by remember { mutableStateOf(false) }
    var isGameOver by remember { mutableStateOf(false) }

    val soundManager: SoundManager = rememberSoundManager()
    val adManager = rememberInterstitialAd()

    val context = LocalContext.current
    val activity = context.findActivity()

    // --- Timer ---
    LaunchedEffect(isGameRunning) {
        if (isGameRunning) {
            val totalTime = 25000L
            val interval = 100L
            var timeElapsed = 0L

            while (timeElapsed < totalTime) {
                delay(interval.milliseconds)
                timeElapsed += interval
                timeLeftProgress = timeElapsed.toFloat() / totalTime.toFloat()
            }

            // timeout
            isGameRunning = false
            isGameOver = score < targetScore
            showDialog = true
        }
    }

    // --- Random Logic ---
    LaunchedEffect(isGameRunning, score) {
        if (isGameRunning) {
            while (true) {
                val nextIndex = (0..8).filter { it != activeMoleIndex }.random()
                activeMoleIndex = nextIndex

                val delaySpeed = (3000L - (level * 300L)).coerceAtLeast(1000L)
                delay(delaySpeed.milliseconds)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        val bgGradientLv = Brush.verticalGradient(
            colors = listOf(
                colorResource(id = R.color.color_a05a2c),
                colorResource(id = R.color.color_783e19)
            )
        )

        Image(
            painter = painterResource(id = R.drawable.bg_whack_mole),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(86.dp)) // Jarak pengganti Appbar

            // Baris Status: Skor, Level, Timer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Komponen Poin
                Box(contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(90.dp, 30.dp)
                        .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                        .border(width = 3.dp, color = Color.Black, shape = RoundedCornerShape(10.dp))) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.star), // Anggap ini R.drawable.star
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${score.toString().padStart(2, '0')} /",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "$targetScore",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Komponen Level
                Text(
                    text = stringResource(id = R.string.value_level, level),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.yellow), // Sesuaikan dengan warnamu
                    modifier = Modifier
                        .background(brush = bgGradientLv, shape = RoundedCornerShape(8.dp))
                        .border(width = 3.dp, color = colorResource(id = R.color.color_4a2306), shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 14.dp, vertical = 4.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Progress Bar Waktu
                LinearProgressIndicator(
                    progress = { timeLeftProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp),
                    color = colorResource(id = R.color.red), // Sesuaikan
                    trackColor = Color.White
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3), // 3 Kolom
                    modifier = Modifier.padding(bottom = 32.dp),
                ) {
                    items(9) { index ->
                        val isShowingPig = (index == activeMoleIndex) && isGameRunning

                        // 1. Setup Animasi Naik Turun (Translation Y)
                        val pigTranslationY by animateFloatAsState(
                            targetValue = if (isShowingPig) 0f else 150f,
                            animationSpec = tween(durationMillis = 200), // setDuration(200)
                            label = "PigYAnimation"
                        )

                        // 2. Setup Animasi Transparansi (Alpha)
                        val pigAlpha by animateFloatAsState(
                            targetValue = if (isShowingPig) 1f else 0f,
                            animationSpec = tween(durationMillis = 200),
                            label = "PigAlphaAnimation"
                        )

                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .padding(8.dp)
                                .clickable(enabled = isShowingPig,
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }) {
                                    score += 1
                                    soundManager.playClick()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_mud_new),
                                contentDescription = null
                            )

                            if (isShowingPig) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_pig),
                                    contentDescription = "Pig",
                                    modifier = Modifier.graphicsLayer {
                                        translationY = pigTranslationY
                                        alpha = pigAlpha
                                    }.padding(bottom = 8.dp, start = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        GameStatusDialog(
            score = score,
            isGameOver = isGameOver,
            onActionClick = {
                showDialog = false
                if (isGameOver) {
                    if (activity != null) {
                        adManager.showAd(activity) {
                            activity.runOnUiThread {
                                onNavigateBack()
                            }
                        }
                    }else {
                        onNavigateBack()
                    }
                } else {
                    // next level
                    level += 1
                    targetScore += level + 2
                    score = 0
                    timeLeftProgress = 0f
                    isGameRunning = true // Restart game
                }
            }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PlayMenuScreenPreview() {
    PlayScreen(onNavigateBack = {})
}