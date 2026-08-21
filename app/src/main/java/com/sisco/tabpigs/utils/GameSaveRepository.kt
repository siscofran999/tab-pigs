package com.sisco.tabpigs.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "tab_pigs_save")

class GameSaveRepository(private val context: Context) {

    private fun getEmptyKey(id: Int) = booleanPreferencesKey("slot_${id}_is_empty")
    private fun getLevelKey(id: Int) = intPreferencesKey("slot_${id}_level")

    val getSlots: Flow<List<SaveSlotData>> = context.dataStore.data.map { prefs ->
        val slots = mutableListOf<SaveSlotData>()

        for (i in 1..3) {
            val isEmpty = prefs[getEmptyKey(i)] ?: true
            val level = prefs[getLevelKey(i)] ?: 1

            slots.add(SaveSlotData(id = i, level = level, isEmpty = isEmpty))
        }
        slots
    }

    fun getSlotById(id: Int): Flow<SaveSlotData> {
        return getSlots.map { slotList ->
            slotList.firstOrNull { it.id == id } ?: SaveSlotData(id = id, level = 1, isEmpty = true)
        }
    }

    suspend fun updateSlot(newSlot: SaveSlotData) {
        context.dataStore.edit { prefs ->
            prefs[getEmptyKey(newSlot.id)] = newSlot.isEmpty
            prefs[getLevelKey(newSlot.id)] = newSlot.level
        }
    }
}

data class SaveSlotData(
    val id: Int,
    val level: Int = 0,
    val isEmpty: Boolean = true
)