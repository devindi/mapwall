package com.devindi.wallpaper.settings.model

import android.content.SharedPreferences
import android.support.annotation.StringRes

open class BooleanField(
    private val storage: SharedPreferences,
    @StringRes override val titleId: Int,
    override val key: String
) : SettingsField<Boolean>() {

    override fun get(): Boolean {
        return get(false)
    }

    override fun get(fallback: Boolean): Boolean {
        return storage.getBoolean(key, fallback)
    }

    override fun set(value: Boolean) {
        super.set(value)
        storage.edit().putBoolean(key, value).apply()
    }
}