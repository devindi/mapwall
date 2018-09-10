package com.devindi.wallpaper.settings.model

import android.content.SharedPreferences
import android.support.annotation.StringRes

class IntField(private val storage: SharedPreferences, @StringRes public val titleId: Int, override val key: String): SettingsField<Int> {

    override fun get(): Int {
        return get(0)
    }

    override fun get(fallback: Int): Int {
        return storage.getInt(key, fallback)
    }

    override fun set(value: Int) {
        storage.edit().putInt(key, value).apply()
    }
}
