package com.devindi.wallpaper.preview

import android.app.WallpaperManager as WallpaperManagerImpl
import android.graphics.Bitmap
import com.devindi.wallpaper.misc.WallpaperConsumer

class WallpaperManager(private val impl: WallpaperManagerImpl) : WallpaperConsumer {

    override fun apply(wallpaper: Bitmap) {
        impl.setBitmap(wallpaper)
    }
}