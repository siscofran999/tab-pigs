package com.sisco.tabpigs.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sisco.tabpigs.Globals.NAV_MAIN_MENU
import com.sisco.tabpigs.Globals.NAV_PLAY_SCREEN
import com.sisco.tabpigs.ui.screen.MainMenuScreen
import com.sisco.tabpigs.ui.screen.PlayScreen

@Composable
fun GameNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NAV_MAIN_MENU) {

        // Rute 1: Halaman Menu Utama
        composable(NAV_MAIN_MENU) {
            MainMenuScreen(
                onPlayClick = {
                    // Berpindah ke halaman play saat tombol ditekan
                    navController.navigate("play_screen")
                }
            )
        }

        // Rute 2: Halaman Play / Game Berlangsung
        composable(NAV_PLAY_SCREEN) {
            PlayScreen(
                onNavigateBack = {
                    // Kembali ke menu utama (misal saat game over)
                    navController.popBackStack("main_menu", inclusive = false)
                }
            )
        }
    }
}