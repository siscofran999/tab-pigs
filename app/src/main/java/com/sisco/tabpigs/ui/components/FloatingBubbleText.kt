package com.sisco.tabpigs.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sisco.tabpigs.utils.ItemType
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun FloatingBubbleText(
    itemType: ItemType,
    onAnimationFinished: () -> Unit
) {
    var isAnimating by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isAnimating = true
        delay(400.milliseconds)
        onAnimationFinished()
    }

    // Animasi posisi Y (Melayang ke atas)
    val offsetY by animateFloatAsState(
        targetValue = if (isAnimating) -120f else 0f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "Floating Y"
    )

    // Animasi Transparansi (Memudar menghilang)
    val alpha by animateFloatAsState(
        targetValue = if (isAnimating) 0f else 1f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "Fade Out"
    )

    // Menentukan Teks dan Warna berdasarkan Tipe
    val (text, fillColor) = when (itemType) {
        ItemType.NORMAL -> "+1" to Color(0xFF4ADE80)
        ItemType.BOMB -> "-1" to Color(0xFFF87171)
        ItemType.GOLDEN -> "+4" to Color(0xFFFFD54F)
    }

    Box(
        modifier = Modifier
            .offset(y = offsetY.dp)
            .alpha(alpha),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = if (itemType == ItemType.GOLDEN) 20.sp else 28.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFF4A2306),
            textAlign = TextAlign.Center,
            style = TextStyle.Default.copy(
                drawStyle = Stroke(miter = 10f, width = 12f, join = StrokeJoin.Round),
                shadow = Shadow(color = Color(0x66000000), offset = Offset(0f, 4f), blurRadius = 4f)
            ),
            lineHeight = 22.sp
        )
        Text(
            text = text,
            fontSize = if (itemType == ItemType.GOLDEN) 20.sp else 28.sp,
            fontWeight = FontWeight.Black,
            color = fillColor,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
    }
}