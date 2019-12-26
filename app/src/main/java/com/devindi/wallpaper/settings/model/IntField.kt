package com.devindi.wallpaper.settings.model

import android.content.SharedPreferences
import androidx.annotation.StringRes

open class IntField(
    storage: SharedPreferences,
    @StringRes override val titleId: Int,
    override val key: String
) : SharedPreferencesField<Int>(storage) {

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
