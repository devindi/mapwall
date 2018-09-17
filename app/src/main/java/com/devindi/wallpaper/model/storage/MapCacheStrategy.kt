package com.devindi.wallpaper.model.storage

import android.content.Context
import android.os.Build
import android.os.Environment
import com.devindi.wallpaper.model.AndroidInfo
import java.io.File

class MapCacheStrategy(private val context: Context, private val androidInfo: AndroidInfo) {

    /**
     * Defines default storage for osm cache.
     * @return path to cache or null if user should select it
     */
    fun defaultCachePath(): String? {
        return if (androidInfo.version() < Build.VERSION_CODES.M) {
            if (isExternalStorageWritable()) {
                createExternalPath()
            } else {
                createInternalPath()
            }
        } else {
            if (isExternalStorageWritable()) {
                null
            } else {
                createInternalPath()
            }
        }
    }

    private fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    fun createInternalPath() = context.filesDir.absolutePath

    fun createExternalPath() = File(context.getExternalFilesDir(null), "map-cache").absolutePath
}
