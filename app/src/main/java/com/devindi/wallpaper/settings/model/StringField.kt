package com.devindi.wallpaper.settings.model

import android.content.SharedPreferences
import android.support.annotation.StringRes

abstract class StringField(
    private val storage: SharedPreferences,
    @StringRes override val titleId: Int,
    override val key: String
) : SettingsField<String>() {

    override fun get(): String {
        return get("")
    }

    override fun get(fallback: String): String {
        return storage.getString(key, fallback)
    }

    override fun set(value: String) {
        storage.edit().putString(key, value).apply()
    }
}
