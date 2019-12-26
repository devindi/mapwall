package com.devindi.wallpaper.settings.model

import android.content.SharedPreferences
import androidx.annotation.StringRes

open class BooleanField(
    storage: SharedPreferences,
    @StringRes override val titleId: Int,
    override val key: String
) : SharedPreferencesField<Boolean>(storage) {

    override fun get(): Boolean {
        return get(false)
    }

    override fun get(fallback: Boolean): Boolean {
        return storage.getBoolean(key, fallback)
    }

    override fun set(value: Boolean) {
        storage.edit().putBoolean(key, value).apply()
    }
}