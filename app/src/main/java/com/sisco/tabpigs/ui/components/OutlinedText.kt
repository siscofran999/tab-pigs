package com.sisco.tabpigs.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun OutlinedText(
    text: String,
    fillColor: Color,
    outlineColor: Color,
    fontSize: androidx.compose.ui.unit.TextUnit,
    outlineWidth: Float = 12f
) {
    Box(contentAlignment = Alignment.Center) {
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = FontWeight.ExtraBold,
            color = outlineColor,
            style = TextStyle.Default.copy(
                drawStyle = Stroke(
                    miter = 10f,
                    width = outlineWidth,
                    join = StrokeJoin.Round
                ),
                shadow = Shadow(
                    color = Color(0x66000000),
                    offset = Offset(0f, 6f),
                    blurRadius = 4f
                )
            )
        )
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = FontWeight.ExtraBold,
            color = fillColor
        )
    }
}