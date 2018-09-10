package com.devindi.wallpaper.settings.model

import android.content.SharedPreferences
import com.devindi.wallpaper.R

const val DIMENSION_HEIGHT = "wallpaper_height"
const val DIMENSION_WIDTH = "wallpaper_width"

class SettingsManager(storage: SharedPreferences) {

    private val map = HashMap<String, SettingsField<*>>()

    init {
        addField(IntField(storage, R.string.settings_item_height, DIMENSION_HEIGHT))
        addField(IntField(storage, R.string.settings_item_width, DIMENSION_WIDTH))
    }

    fun getIntField(key: String) : IntField {
        return map[key] as IntField
    }

    private fun addField(field : SettingsField<*>) {
        map[field.key] = field
    }
}
