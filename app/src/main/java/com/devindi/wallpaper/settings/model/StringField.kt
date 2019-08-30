package com.devindi.wallpaper.settings.model

import android.content.SharedPreferences

abstract class StringField(
    private val storage: SharedPreferences,
    override val key: String
) : SettingsField<String>() {

    override fun get(): String {
        return get("")
    }

    override fun get(fallback: String): String {
        return storage.getString(key, fallback)
    }

    override fun set(value: String) {
        super.set(value)
        storage.edit().putString(key, value).apply()
    }
}
