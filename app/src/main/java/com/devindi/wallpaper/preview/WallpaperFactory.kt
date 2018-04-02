package com.devindi.wallpaper.preview

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import org.osmdroid.tileprovider.cachemanager.CacheManager
import org.osmdroid.tileprovider.modules.IFilesystemCache
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.MapTileIndex

class WallpaperFactory(private val cacheManager: CacheManager, private var tileSource: OnlineTileSourceBase, private val cache: IFilesystemCache) {

    fun createWallpaper(rect: BoundingBox, zoom: Int): Bitmap {
        var tilesCoverage = CacheManager.getTilesCoverage(rect, zoom)
        tilesCoverage.forEach {
            if (cacheManager.downloadingAction.tileAction(it)) {
                throw IllegalStateException("Failed to load tile")
            }
        }
        tilesCoverage = tilesCoverage.sortedWith(compareBy({ MapTileIndex.getX(it) }, { MapTileIndex.getY(it) }))
        val startX = MapTileIndex.getX(tilesCoverage[0])
        val tilesY = tilesCoverage
                .filter { MapTileIndex.getX(it) == startX }
                .size
        val tilesX = tilesCoverage.size / tilesY
        val tileSize = tileSource.tileSizePixels
        val target = Bitmap.createBitmap(tilesX * tileSize, tilesY * tileSize, Bitmap.Config.RGB_565)
        val canvas = Canvas(target)
        for (i in 0 until tilesCoverage.size) {
            val pxLeft = (i / tilesY) * tileSize.toFloat()
            val pxTop = (i % tilesY) * tileSize.toFloat()
            val drawable = cache.loadTile(tileSource, tilesCoverage[i]) as BitmapDrawable
            canvas.drawBitmap(drawable.bitmap, pxLeft, pxTop, null)
        }
        return target
    }
}