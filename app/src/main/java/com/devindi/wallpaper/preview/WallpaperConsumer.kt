package com.devindi.wallpaper.preview

import android.app.WallpaperManager
import android.graphics.Bitmap

class WallpaperConsumer(private var manager: WallpaperManager) {

    fun setWallpaper(bitmap: Bitmap?) {
        manager.setBitmap(bitmap)
    }
}