package com.sisco.tabpigs.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sisco.tabpigs.R
import com.sisco.tabpigs.ui.components.OutlinedText
import com.sisco.tabpigs.ui.components.SaveItem
import com.sisco.tabpigs.utils.GameSaveRepository

@Composable
fun SaveScreen(
    onNavigateBack: () -> Unit,
    onPlayClick: (Int) -> Unit
) {

    val context = LocalContext.current
    val saveGameRepo = remember { GameSaveRepository(context) }
    val slots by saveGameRepo.getSlots.collectAsState(initial = emptyList())

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_whack_mole),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(modifier = Modifier.fillMaxSize(). padding(start = 12.dp, end = 12.dp)) {
            Spacer(modifier = Modifier.height(86.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(color = colorResource(id = R.color.color_a96628))
                        .border(
                            width = 4.dp,
                            color = colorResource(id = R.color.color_482307),
                            shape = CircleShape
                        )
                        .clickable {
                            onNavigateBack()
                        }, contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedText(
                        text = stringResource(id = R.string.choose_slot),
                        fillColor = colorResource(id = R.color.color_ffd54f), // Kuning keemasan
                        outlineColor = colorResource(id = R.color.color_63320c),
                        fontSize = 24.sp,
                        outlineWidth = 14f
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    OutlinedText(
                        text = stringResource(R.string.your_progress_game),
                        fillColor = colorResource(id = R.color.white),
                        outlineColor = colorResource(id = R.color.color_63320c),
                        fontSize = 18.sp,
                        outlineWidth = 10f
                    )
                }
                Spacer(modifier = Modifier.size(24.dp))
            }

            Spacer(modifier = Modifier.size(68.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                slots.forEach { slot ->
                    SaveItem(slot = slot, onPlayClick = { id ->
                        onPlayClick(id)
                    })
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun MemoryScreenPreview() {
    SaveScreen(onNavigateBack = {}, onPlayClick = {})
}