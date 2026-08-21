package com.sisco.tabpigs.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sisco.tabpigs.R
import com.sisco.tabpigs.ui.screen.SaveScreen
import com.sisco.tabpigs.utils.SaveSlotData
import com.sisco.tabpigs.utils.dashedBorder

@Composable
fun SaveItem(slot: SaveSlotData) {
    // Warna-warna yang diambil dari desainmu
    val filledBgColor = Brush.verticalGradient(
        colors = listOf(
            colorResource(id = R.color.color_c78748),
            colorResource(id = R.color.color_a66325)
        )
    )
    val emptyBgColor = Brush.verticalGradient(
        colors = listOf(
            colorResource(id = R.color.color_503e15),
            colorResource(id = R.color.color_503e15)
        )
    )

    val filledBorderColor = Color(0xFF482307) // Cokelat gelap untuk border & text
    val emptyBorderColor = colorResource(id = R.color.color_995b25) // Cokelat pudar untuk dashed border

    val badgeBgColor = if (slot.isEmpty) colorResource(id = R.color.color_b1b0aa) else colorResource(R.color.color_ffd54f)
    val badgeTextColor = if (slot.isEmpty) colorResource(id = R.color.color_38312c) else colorResource(id = R.color.color_482307)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            // Padding bawah dan kanan opsional agar shadow/elemen tidak terpotong
            .padding(bottom = 8.dp)
    ) {
        // --- 1. KOTAK UTAMA (SLOT) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, top = 12.dp) // Memberi ruang untuk badge di kiri atas
                .height(90.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(if (slot.isEmpty) emptyBgColor else filledBgColor)
                .then(
                    if (slot.isEmpty) {
                        Modifier.dashedBorder(emptyBorderColor, 4.dp, 10.dp)
                    } else {
                        Modifier.border(4.dp, filledBorderColor, RoundedCornerShape(10.dp))
                    }
                )
                .clickable {
                    if (slot.isEmpty) {
                        // Aksi saat slot kosong diklik (Create New Game)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            if (slot.isEmpty) {
                // TAMPILAN KOSONG
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "+",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = emptyBorderColor
                    )
                    Text(
                        text = "KOSONG",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = emptyBorderColor
                    )
                }
            } else {
                // TAMPILAN TERISI
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Ikon Babi (Silakan sesuaikan R.drawable-nya)
                    Image(
                        painter = painterResource(id = R.drawable.ic_pig),
                        contentDescription = "Pig Icon",
                        modifier = Modifier.size(64.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "LEVEL ${slot.level.toString().padStart(2, '0')}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )

                    // Tombol Play
                    Box(
                        modifier = Modifier
                            .size(48.dp) // Ukuran total tombol
                            .clip(RoundedCornerShape(10.dp)) // Memotong sudut agar efek klik (ripple) tidak keluar garis
                            .background(color = colorResource(id = R.color.color_4ade80))
                            .border(3.dp, colorResource(id = R.color.color_166534), RoundedCornerShape(10.dp))
                            .clickable { /* Aksi Load Game */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_play_btn),
                            contentDescription = "Play",
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Tombol Hapus
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(color = colorResource(id = R.color.color_f87171))
                            .border(3.dp, colorResource(id = R.color.color_991b1b), RoundedCornerShape(10.dp))
                            .clickable { /* Aksi Load Game */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_trash_btn),
                            contentDescription = "Delete",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

        // --- 2. BADGE ANGKA (Melayang di atas kotak) ---
        Box(
            modifier = Modifier
                .align(Alignment.TopStart) // Tempel di kiri atas Box induk
                .size(32.dp)
                .clip(CircleShape)
                .background(badgeBgColor)
                .border(4.dp, badgeTextColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = slot.id.toString(),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = badgeTextColor
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SaveItemPreview() {
    SaveItem(SaveSlotData(id = 1, level = 1, isEmpty = false))
}