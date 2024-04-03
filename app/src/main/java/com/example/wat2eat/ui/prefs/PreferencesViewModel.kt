package com.example.wat2eat.ui.prefs

import androidx.lifecycle.ViewModel
import com.example.wat2eat.data.preferences.samplePreferences
import com.example.wat2eat.models.Preference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PreferencesViewModel: ViewModel() {
    private val _uiState: MutableStateFlow<PrefsUiState>
    val uiState: StateFlow<PrefsUiState>
    init {
        _uiState = MutableStateFlow(PrefsUiState(prefs = samplePreferences))
        uiState = _uiState.asStateFlow()
    }

    fun updatePrefs() {
        //takes all the modified preferences (activated/deactivated) from the activePrefs and updates
        // them in the prefs list.
        _uiState.update { currentState ->
            if (currentState.activePrefs != null) {
                for(modifiedPref in currentState.activePrefs) {
                    for(pref in currentState.prefs) {
                        if(pref.name == modifiedPref.name){
                            pref.active = modifiedPref.active
                        }
                    }
                }
            }
            currentState.copy(
                prefs = currentState.prefs,
                activePrefs = mutableListOf(),
                isBottomSheetOpen = currentState.isBottomSheetOpen,
            )

        }
    }

    fun getActivePrefs(): List<Preference> {
        val list: List<Preference> = listOf()
        for (pref in _uiState.value.prefs){
            if(pref.active) {
                list.plus(pref)
            }
        }
        return list
    }

    fun toggleBottomSheet() {
        _uiState.update { currentState ->
            currentState.copy(
                prefs = currentState.prefs,
                activePrefs = currentState.activePrefs,
                isBottomSheetOpen = !currentState.isBottomSheetOpen
            )
        }
    }



}