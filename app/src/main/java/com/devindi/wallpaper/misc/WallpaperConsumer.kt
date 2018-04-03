package com.devindi.wallpaper.misc

import android.graphics.Bitmap

interface WallpaperConsumer {
    fun apply(wallpaper: Bitmap)
}