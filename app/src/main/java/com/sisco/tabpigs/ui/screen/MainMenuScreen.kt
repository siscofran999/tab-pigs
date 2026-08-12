package com.sisco.tabpigs.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sisco.tabpigs.R

@Composable
fun MainMenuScreen(onPlayClick: () -> Unit, isMuted: Boolean, onMuteToggle: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.bg_whack_mole),
            contentDescription = "Background",
            contentScale = ContentScale.Crop, // Membuat gambar memenuhi layar penuh
            modifier = Modifier.fillMaxSize()
        )

        val img = if (isMuted) {
            R.drawable.ic_mute
        }else {
            R.drawable.ic_unmute
        }
        Image(
            painter = painterResource(id = img),
            contentDescription = "volume",
            modifier = Modifier.align(Alignment.TopEnd)
                .padding(top = 36.dp, end = 24.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ){
                    onMuteToggle.invoke()
                }
        )

        // 2. Susunan Vertikal Konten
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally // Rata tengah
        ) {
            Spacer(modifier = Modifier.height(60.dp)) // Margin top 60dp

            // Gambar Home
            Image(
                painter = painterResource(id = R.drawable.img_home),
                contentDescription = "Home Title",
                modifier = Modifier.fillMaxWidth()
            )

            // Baris Rumput Atas (1, 2, 3)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween // Meratakan jarak antar rumput secara otomatis
            ) {
                Image(painter = painterResource(id = R.drawable.ic_grass), contentDescription = null)
                Image(painter = painterResource(id = R.drawable.ic_grass), contentDescription = null)
                Image(painter = painterResource(id = R.drawable.ic_grass), contentDescription = null)
            }

            Spacer(modifier = Modifier.height(40.dp)) // Margin top 40dp

            Image(
                painter = painterResource(id = R.drawable.img_btn_play),
                contentDescription = "Play Button",
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onPlayClick() // Panggil aksi saat diklik
                }
            )

            Spacer(modifier = Modifier.height(40.dp)) // Margin top 40dp

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 36.dp), // Margin start & end 36dp
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(painter = painterResource(id = R.drawable.ic_grass), contentDescription = null)
                Image(painter = painterResource(id = R.drawable.ic_grass), contentDescription = null)
                Image(painter = painterResource(id = R.drawable.ic_grass), contentDescription = null)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun MainMenuScreenPreview() {
    MainMenuScreen(onPlayClick = {}, isMuted = true, onMuteToggle = {})
}