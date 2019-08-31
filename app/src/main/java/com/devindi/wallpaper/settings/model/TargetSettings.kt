package com.devindi.wallpaper.settings.model

import android.content.SharedPreferences
import com.devindi.wallpaper.R

enum class WallpaperMode { SYSTEM, LOCK, BOTH }

class WallpaperModeField(
    storage: SharedPreferences
) : StringField(storage, R.string.settings_item_wallpaper_mode, "wallpaper_mode")

class WallpaperTargetField(
    storage: SharedPreferences
) : BooleanField(storage, R.string.settings_item_wallpaper, "target_wallpaper")

class StorageTargetField(
    storage: SharedPreferences
) : BooleanField(storage, R.string.settings_item_storage, "target_storage")
