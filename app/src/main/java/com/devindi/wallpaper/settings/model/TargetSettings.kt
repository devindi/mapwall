package com.devindi.wallpaper.settings.model

import android.content.SharedPreferences
import com.devindi.wallpaper.R

class WallpaperTargetField(storage: SharedPreferences) : BooleanField(storage, R.string.settings_item_wallpaper, "target_wallpaper")

class StorageTargetField(storage: SharedPreferences) : BooleanField(storage, R.string.settings_item_storage, "target_storage")
