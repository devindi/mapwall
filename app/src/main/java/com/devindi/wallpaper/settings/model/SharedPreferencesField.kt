package com.devindi.wallpaper.settings.model

import android.content.SharedPreferences

abstract class SharedPreferencesField<T>(
    protected val storage: SharedPreferences
) : SettingsField<T>() {

    private val preferencesListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _: SharedPreferences, key: String ->
            if (key == this.key) {
                liveData.value = get()
            }
        }

    init {
        storage.registerOnSharedPreferenceChangeListener(preferencesListener)
    }
}
