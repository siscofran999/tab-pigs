package com.sisco.tabpigs.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sisco.tabpigs.utils.Globals.NAV_MAIN_MENU
import com.sisco.tabpigs.utils.Globals.NAV_PLAY_SCREEN
import com.sisco.tabpigs.ui.screen.MainMenuScreen
import com.sisco.tabpigs.ui.screen.PlayScreen
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sisco.tabpigs.utils.BackgroundMusicManager

@Composable
fun GameNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isGameRunning = currentRoute == NAV_PLAY_SCREEN
    var isMuted by remember { mutableStateOf(false) }
    BackgroundMusicManager(isGameRunning, isMuted)

    NavHost(navController = navController, startDestination = NAV_MAIN_MENU) {
        composable(NAV_MAIN_MENU) {
            MainMenuScreen(
                onPlayClick = {
                    navController.navigate("play_screen")
                },
                isMuted = isMuted,
                onMuteToggle = {
                    isMuted = !isMuted
                }
            )
        }

        composable(NAV_PLAY_SCREEN) {
            PlayScreen(
                onNavigateBack = {
                    navController.popBackStack("main_menu", inclusive = false)
                }
            )
        }
    }
}