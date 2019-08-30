package com.devindi.wallpaper.home

import android.app.WallpaperManager
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.support.annotation.RequiresApi
import com.devindi.wallpaper.settings.model.BooleanField
import com.devindi.wallpaper.settings.model.StorageTargetField
import com.devindi.wallpaper.settings.model.WallpaperTargetField
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.util.Date

enum class Target { SYSTEM, LOCK, BOTH }

fun createWallpaperHandler(impl: WallpaperManager, storage: SharedPreferences): WallpaperHandler {
    val wallpaperTarget = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        WallpaperHandlerApiNImpl(impl)
    } else {
        WallpaperHandlerImpl(impl)
    }
    return CompositeHandler(WallpaperTargetField(storage), wallpaperTarget, StorageTargetField(storage), ExportWallpaperHandler(Environment.getExternalStoragePublicDirectory("Maps")))
}

interface WallpaperHandler {

    fun handle(wallpaper: Bitmap, target: Target)
}

class CompositeHandler(
    private val wallpaperTargetField: BooleanField,
    private val wallpaperTarget: WallpaperHandler,
    private val storageTargetField: BooleanField,
    private val storageTarget: WallpaperHandler
): WallpaperHandler {

    override fun handle(wallpaper: Bitmap, target: Target) {
        if (wallpaperTargetField.get()) {
            wallpaperTarget.handle(wallpaper, target)
        }
        if (storageTargetField.get()) {
            storageTarget.handle(wallpaper, target)
        }
    }
}

/**
 * Wallpaper handler implementation for Android 7.0+
 * Setup wallpaper to system, lock screen or both
 */
@RequiresApi(Build.VERSION_CODES.N)
class WallpaperHandlerApiNImpl(private val impl: WallpaperManager) : WallpaperHandler {

    override fun handle(wallpaper: Bitmap, target: Target) {
        val flag = when (target) {
            Target.SYSTEM -> WallpaperManager.FLAG_SYSTEM
            Target.LOCK -> WallpaperManager.FLAG_LOCK
            Target.BOTH -> WallpaperManager.FLAG_LOCK or WallpaperManager.FLAG_SYSTEM
        }
        impl.setBitmap(wallpaper, null, true, flag)
    }
}

/**
 * Wallpaper handler implementation before Android 7.0
 * This implementation may be used on 7.0+ but target parameter will be ignored
 */
class WallpaperHandlerImpl(private val impl: WallpaperManager) : WallpaperHandler {

    override fun handle(wallpaper: Bitmap, target: Target) {
        impl.setBitmap(wallpaper)
    }
}

/**
 * Useful for debug handler implementation
 */
class ExportWallpaperHandler(private val targetDir: File) : WallpaperHandler {

    override fun handle(wallpaper: Bitmap, target: Target) {
        if (!targetDir.exists()) {
            targetDir.mkdirs()
        }
        val targetFile = File(targetDir, "${Date().time}.png")
        Timber.d("Saving to file %s", targetFile.absolutePath)
        targetFile.createNewFile()
        FileOutputStream(targetFile).use {
            wallpaper.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
    }
}