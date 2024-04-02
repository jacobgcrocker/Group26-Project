package com.example.wat2eat.models

import com.example.wat2eat.data.preferences.samplePreferences

@Deprecated("No longer in use but was part of earlier iteration of preferences")
class Preferences(

) {
    val activePreferences: MutableSet<Preference> = mutableSetOf()
    val allPreferences: List<Preference> = samplePreferences

    private fun changeActiveState(pref: Preference) {
        if(pref.active){
            activePreferences.remove(pref)
        }
        else {
            activePreferences.add(pref)
        }
        pref.active = !pref.active

    }


    fun get_prefs(category: String): MutableSet<Preference>{
        val prefs: MutableSet<Preference> = mutableSetOf()
        for(pref in allPreferences){
            if(pref.category == category) {
                prefs.add(pref)
            }
        }
        return prefs
    }


}