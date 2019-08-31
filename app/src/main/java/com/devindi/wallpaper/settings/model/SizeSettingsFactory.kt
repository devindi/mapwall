package com.devindi.wallpaper.settings.model

import android.content.SharedPreferences
import com.devindi.wallpaper.R
import com.devindi.wallpaper.model.DisplayInfo

const val DIMENSION_HEIGHT = "wallpaper_height"
const val DIMENSION_WIDTH = "wallpaper_width"

class SizeSettingsFactory(
    private val storage: SharedPreferences,
    private val displayInfo: DisplayInfo
) {

    fun createField(key: String): IntField {
        return when (key) {
            DIMENSION_WIDTH -> WidthField(storage, displayInfo)
            DIMENSION_HEIGHT -> HeightField(storage, displayInfo)
            else -> throw IllegalArgumentException("Unknown key: $key")
        }
    }
}

class HeightField(
    storage: SharedPreferences,
    private val displayInfo: DisplayInfo
) : IntField(storage, R.string.settings_item_height, DIMENSION_HEIGHT) {

    override fun get(): Int {
        return get(displayInfo.screenHeight())
    }
}

class WidthField(
    storage: SharedPreferences,
    private val displayInfo: DisplayInfo
) : IntField(storage, R.string.settings_item_width, DIMENSION_WIDTH) {

    override fun get(): Int {
        return get(displayInfo.screenWidth())
    }
}
