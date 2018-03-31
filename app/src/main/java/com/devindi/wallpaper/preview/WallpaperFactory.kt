package com.devindi.wallpaper.preview

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import org.osmdroid.tileprovider.cachemanager.CacheManager
import org.osmdroid.tileprovider.modules.IFilesystemCache
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase

class WallpaperFactory(private val cacheManager: CacheManager, private val cache: IFilesystemCache) {

    fun createWallpaper(tileSource: OnlineTileSourceBase, tileIndex: Long): Bitmap {
        val loaded = cacheManager.loadTile(tileSource, tileIndex)
        if (loaded) {
            val drawable = cache.loadTile(tileSource, tileIndex) as BitmapDrawable
            return drawable.bitmap
        }
        else throw IllegalStateException("Failed to load tile")
    }
}