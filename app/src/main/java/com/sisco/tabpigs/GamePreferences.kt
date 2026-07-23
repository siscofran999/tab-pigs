package com.sisco.tabpigs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore(name = "tab_pigs_settings")

class GamePreferences(private val context: Context) {

    private companion object {
        val LAST_LEVEL_KEY = intPreferencesKey("last_level")
        val HAS_SAVED_GAME_KEY = booleanPreferencesKey("has_saved_game")
    }

    suspend fun saveProgress(level: Int, hasSaved: Boolean = true) {
        context.dataStore.edit { preferences ->
            preferences[LAST_LEVEL_KEY] = level
            preferences[HAS_SAVED_GAME_KEY] = hasSaved
        }
    }

    suspend fun getLastLevel(): Int {
        val preferences = context.dataStore.data.first()
        return preferences[LAST_LEVEL_KEY] ?: 1 // Default level 1
    }

    suspend fun hasSavedGame(): Boolean {
        val preferences = context.dataStore.data.first()
        return preferences[HAS_SAVED_GAME_KEY] ?: false
    }
}