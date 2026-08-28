package com.sisco.tabpigs.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sisco.tabpigs.utils.Globals.NAV_MAIN_MENU
import com.sisco.tabpigs.utils.Globals.NAV_PLAY_SCREEN
import com.sisco.tabpigs.ui.screen.MainMenuScreen
import com.sisco.tabpigs.ui.screen.PlayScreen
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.sisco.tabpigs.ui.screen.SaveScreen
import com.sisco.tabpigs.utils.BackgroundMusicManager
import com.sisco.tabpigs.utils.Globals.NAV_SAVE_SCREEN

@Composable
fun GameNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isGameRunning = currentRoute == "$NAV_PLAY_SCREEN/{slotId}"
    var isMuted by remember { mutableStateOf(false) }
    BackgroundMusicManager(isGameRunning, isMuted)

    NavHost(navController = navController, startDestination = NAV_MAIN_MENU) {
        composable(NAV_MAIN_MENU) {
            MainMenuScreen(
                onPlayClick = { slotId ->
                    navController.navigate("$NAV_PLAY_SCREEN/$slotId")
                },
                onNavigateToSaveScreen = {
                    navController.navigate(NAV_SAVE_SCREEN)
                },
                isMuted = isMuted,
                onMuteToggle = {
                    isMuted = !isMuted
                }
            )
        }

        composable(
            route = "$NAV_PLAY_SCREEN/{slotId}",
            arguments = listOf(
                navArgument("slotId") { type= NavType.IntType }
            )) { backStackEntry ->
            val slotId = backStackEntry.arguments?.getInt("slotId") ?: 1
            PlayScreen(
                onNavigateBack = {
                    navController.popBackStack(NAV_MAIN_MENU, inclusive = false)
                },
                saveSlotId = slotId
            )
        }

        composable(NAV_SAVE_SCREEN) {
            SaveScreen(
                onNavigateBack = {
                    navController.popBackStack(NAV_MAIN_MENU, inclusive = false)
                },
                onPlayClick = { slotId ->
                    navController.navigate("$NAV_PLAY_SCREEN/$slotId")
                }
            )
        }
    }
}