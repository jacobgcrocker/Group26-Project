package com.example.wat2eat.ui.prefs

import com.example.wat2eat.models.Preference

data class PrefsUiState(
    val activePrefs: MutableList<Preference>? = mutableListOf(),
    val prefs: List<Preference>,
    val isBottomSheetOpen: Boolean = false,
)