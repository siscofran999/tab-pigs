package com.sisco.tabpigs.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.core.view.WindowCompat
import com.sisco.tabpigs.R


@Composable
fun TabPigsTheme(
    content: @Composable () -> Unit
) {

    val gameWhite = colorResource(id = R.color.white)
    val gameBlack = colorResource(id = R.color.black)

    val gameColorScheme = lightColorScheme(
//        primary = colorResource(id = R.color.green_bg), // Sesuaikan dengan warna di colors.xml
//        secondary = colorResource(id = R.color.secondary),
        background = gameWhite,
        surface = gameWhite,
        onPrimary = gameWhite,
        onSecondary = gameBlack,
        onBackground = gameBlack,
        onSurface = gameBlack
    )

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Membuat status bar transparan agar background game terlihat penuh
            window.statusBarColor = android.graphics.Color.TRANSPARENT
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = gameColorScheme,
        typography = Typography,
        content = content
    )
}