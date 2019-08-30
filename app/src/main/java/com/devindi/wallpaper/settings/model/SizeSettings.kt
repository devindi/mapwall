package com.devindi.wallpaper.settings.model

import android.content.SharedPreferences
import com.devindi.wallpaper.R

class HeightField(storage: SharedPreferences) : IntField(storage, R.string.settings_item_height, "wallpaper_height")

class WidthField(storage: SharedPreferences) : IntField(storage, R.string.settings_item_width, "wallpaper_width")
