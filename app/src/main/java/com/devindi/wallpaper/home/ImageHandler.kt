package com.devindi.wallpaper.home

import android.app.WallpaperManager
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.support.annotation.RequiresApi
import com.devindi.wallpaper.settings.model.BooleanField
import com.devindi.wallpaper.settings.model.StorageTargetField
import com.devindi.wallpaper.settings.model.WallpaperMode
import com.devindi.wallpaper.settings.model.WallpaperModeField
import com.devindi.wallpaper.settings.model.WallpaperTargetField
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.util.Date

fun createWallpaperHandler(impl: WallpaperManager, storage: SharedPreferences): ImageHandler {
    val wallpaperTarget = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        WallpaperHandlerApiNImpl(impl, WallpaperModeField(storage))
    } else {
        WallpaperHandlerImpl(impl)
    }
    return CompositeHandler(WallpaperTargetField(storage), wallpaperTarget, StorageTargetField(storage), ExportImageHandler(Environment.getExternalStoragePublicDirectory("Maps")))
}

interface ImageHandler {

    fun handle(image: Bitmap)
}

class CompositeHandler(
    private val wallpaperTargetField: BooleanField,
    private val wallpaperTarget: ImageHandler,
    private val storageTargetField: BooleanField,
    private val storageTarget: ImageHandler
): ImageHandler {

    override fun handle(image: Bitmap) {
        if (wallpaperTargetField.get()) {
            wallpaperTarget.handle(image)
        }
        if (storageTargetField.get()) {
            storageTarget.handle(image)
        }
    }
}

/**
 * Wallpaper handler implementation for Android 7.0+
 * Setup wallpaper to system, lock screen or both
 */
@RequiresApi(Build.VERSION_CODES.N)
class WallpaperHandlerApiNImpl(private val impl: WallpaperManager, private val modeField: WallpaperModeField) : ImageHandler {

    override fun handle(image: Bitmap) {
        val flag = when (WallpaperMode.valueOf(modeField.get())) {
            WallpaperMode.SYSTEM -> WallpaperManager.FLAG_SYSTEM
            WallpaperMode.LOCK -> WallpaperManager.FLAG_LOCK
            WallpaperMode.BOTH -> WallpaperManager.FLAG_LOCK or WallpaperManager.FLAG_SYSTEM
        }
        impl.setBitmap(image, null, true, flag)
    }
}

/**
 * Wallpaper handler implementation before Android 7.0
 * This implementation may be used on 7.0+ but target parameter will be ignored
 */
class WallpaperHandlerImpl(private val impl: WallpaperManager) : ImageHandler {

    override fun handle(image: Bitmap) {
        impl.setBitmap(image)
    }
}

/**
 * Useful for debug handler implementation
 */
class ExportImageHandler(private val targetDir: File) : ImageHandler {

    override fun handle(image: Bitmap) {
        if (!targetDir.exists()) {
            targetDir.mkdirs()
        }
        val targetFile = File(targetDir, "${Date().time}.png")
        Timber.d("Saving to file %s", targetFile.absolutePath)
        targetFile.createNewFile()
        FileOutputStream(targetFile).use {
            image.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
    }
}