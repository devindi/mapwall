package com.devindi.wallpaper.settings.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.SharedPreferences
import com.devindi.wallpaper.R

const val DIMENSION_HEIGHT = "wallpaper_height"
const val DIMENSION_WIDTH = "wallpaper_width"

@Deprecated("Same as KeyValueStorage")
class SettingsManager(storage: SharedPreferences) {

    private val map = HashMap<String, SettingsField<*>>()
    private val fieldLive = MutableLiveData<String>()
    private val changeListener = SharedPreferences.OnSharedPreferenceChangeListener {
        _, key -> fieldLive.value = key
    }

    init {
        addField(IntField(storage, R.string.settings_item_height, DIMENSION_HEIGHT))
        addField(IntField(storage, R.string.settings_item_width, DIMENSION_WIDTH))
        storage.registerOnSharedPreferenceChangeListener(changeListener)
    }

    fun getIntField(key: String): IntField {
        return map[key] as IntField
    }

    fun fieldChange(): LiveData<String> = fieldLive

    private fun addField(field: SettingsField<*>) {
        map[field.key] = field
    }
}
