package com.sisco.tabpigs.view

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sisco.tabpigs.R
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun PlayScreen(
    onNavigateBack: () -> Unit
) {
    var score by remember { mutableIntStateOf(0) }
    var level by remember { mutableIntStateOf(1) }
    var targetScore by remember { mutableIntStateOf(30) }

    var isGameRunning by remember { mutableStateOf(true) }
    var activeMoleIndex by remember { mutableIntStateOf(-1) }
    var timeLeftProgress by remember { mutableFloatStateOf(0f) }

    var showDialog by remember { mutableStateOf(false) }
    var isGameOver by remember { mutableStateOf(false) }

    // --- GAME LOGIC: Timer (Pengganti CountDownTimer) ---
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

            // Waktu Habis
            isGameRunning = false
            isGameOver = score < targetScore // Jika poin kurang dari target, maka Game Over
            showDialog = true
        }
    }

    // --- GAME LOGIC: Pemunculan Babi (Pengganti Handler & Runnable) ---
    // Efek ini akan di-restart otomatis setiap kali 'score' bertambah atau game berjalan
    LaunchedEffect(isGameRunning, score) {
        if (isGameRunning) {
            while (true) {
                // Pilih lubang acak yang berbeda dari sebelumnya
                val nextIndex = (0..8).filter { it != activeMoleIndex }.random()
                activeMoleIndex = nextIndex

                // Kalkulasi kecepatan (Semakin tinggi level, semakin cepat)
                val delaySpeed = (3000L - (level * 300L)).coerceAtLeast(1000L)
                delay(delaySpeed.milliseconds)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background
        Image(
            painter = painterResource(id = R.drawable.bg_whack_mole),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(40.dp)) // Jarak pengganti Appbar

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
                    text = "Level $level",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.yellow), // Sesuaikan dengan warnamu
                    modifier = Modifier
                        .background(Color.DarkGray, shape = RoundedCornerShape(8.dp))
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

            Spacer(modifier = Modifier.weight(1f)) // Mendorong Grid ke bawah

            // Grid Play Area (Pengganti RecyclerView)
            LazyVerticalGrid(
                columns = GridCells.Fixed(3), // 3 Kolom
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.padding(bottom = 32.dp)
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

                    // Box sebagai pengganti layout item_play.xml
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .padding(8.dp)
                            .clickable(enabled = isShowingPig) {
                                // Logika klik babi
                                score += 1
                                activeMoleIndex = -1
                                // Catatan: SoundPool bisa dipanggil di sini
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        // Tampilkan lubang kosong
                        Image(
                            painter = painterResource(id = R.drawable.img_mud_new), // Ganti aset lubangmu
                            contentDescription = null
                        )

                        // Tampilkan babi jika indeksnya cocok
                        if (isShowingPig) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_pig), // Ganti aset babimu
                                contentDescription = "Pig",
                                modifier = Modifier.graphicsLayer {
                                    // Menerapkan animasi ke gambar babi ini
                                    translationY = pigTranslationY
                                    alpha = pigAlpha
                                }
                            )
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
                    // Panggil Interstitial Ads di sini, lalu kembali ke menu
                    onNavigateBack()
                } else {
                    // Lanjut Level
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