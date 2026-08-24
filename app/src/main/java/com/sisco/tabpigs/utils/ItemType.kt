package com.sisco.tabpigs.utils

enum class ItemType { NORMAL, BOMB, GOLDEN }

data class FloatingTextData(
    val id: Long = System.currentTimeMillis(),
    val type: ItemType,
    val holeIndex: Int
)