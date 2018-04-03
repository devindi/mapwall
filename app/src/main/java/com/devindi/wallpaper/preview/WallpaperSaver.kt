package com.devindi.wallpaper.preview

import android.graphics.Bitmap
import com.devindi.wallpaper.misc.WallpaperConsumer
import java.io.File
import java.io.FileOutputStream
import java.util.*

class WallpaperSaver(private val targetDir: File) : WallpaperConsumer {
    override fun apply(wallpaper: Bitmap) {
        FileOutputStream(File(targetDir, "${Date()}.png")).use {
            wallpaper.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
    }
}