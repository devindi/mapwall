package com.devindi.wallpaper.home

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import com.devindi.wallpaper.misc.ReportManager
import org.osmdroid.tileprovider.cachemanager.CacheManager
import org.osmdroid.tileprovider.modules.IFilesystemCache
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.MapTileIndex
import timber.log.Timber

class WallpaperFactory(private val cacheManager: CacheManager, private var tileSource: OnlineTileSourceBase, private val cache: IFilesystemCache, private val reportManager: ReportManager) {

    fun createWallpaper(rect: BoundingBox, zoom: Int): Bitmap {
        var tilesCoverage = CacheManager.getTilesCoverage(rect, zoom)
        tilesCoverage.forEach {
            if (cacheManager.downloadingAction.tileAction(it)) {
                val error = IllegalStateException("Failed to load tile")
                reportManager.reportError(error)
                throw error
            }
        }
        tilesCoverage = tilesCoverage.sortedWith(compareBy({ MapTileIndex.getX(it) }, { MapTileIndex.getY(it) }))
        val startX = MapTileIndex.getX(tilesCoverage[0])
        val tilesY = tilesCoverage
                .filter { MapTileIndex.getX(it) == startX }
                .size
        val tilesX = tilesCoverage.size / tilesY
        val tileSize = tileSource.tileSizePixels
        val left = calculateLeftOffset(rect, zoom, tileSize)
        val top = calculateTopOffset(rect, zoom, tileSize)
        val right = calculateRightOffset(rect, zoom, tileSize)
        val bottom = calculateBottomOffset(rect, zoom, tileSize)
        val target = Bitmap.createBitmap(tilesX * tileSize - left - right, tilesY * tileSize - top - bottom, Bitmap.Config.RGB_565)
        val canvas = Canvas(target)
        for (i in 0 until tilesCoverage.size) {
            val pxLeft = (i / tilesY) * tileSize.toFloat() - left
            val pxTop = (i % tilesY) * tileSize.toFloat() - top
            val drawable = cache.loadTile(tileSource, tilesCoverage[i]) as BitmapDrawable
            Timber.d("Drawing tile ${MapTileIndex.getZoom(tilesCoverage[i])} / ${MapTileIndex.getX(tilesCoverage[i])} / ${MapTileIndex.getY(tilesCoverage[i])} at $pxLeft $pxTop ")
            canvas.drawBitmap(drawable.bitmap, pxLeft, pxTop, null)
        }
        return target
    }

    private fun calculateLeftOffset(rect: BoundingBox, zoom: Int, tileSize: Int): Int {
        val tileX = lonToTile(rect.lonWest, zoom)
        val relative = tileX % 1
        return (tileSize * relative).toInt()
    }

    private fun calculateTopOffset(rect: BoundingBox, zoom: Int, tileSize: Int): Int {
        val tileY = latToTile(rect.latNorth, zoom)
        val relative = tileY % 1
        return (tileSize * relative).toInt()
    }

    private fun calculateRightOffset(rect: BoundingBox, zoom: Int, tileSize: Int): Int {
        val tileX = lonToTile(rect.lonEast, zoom)
        val relative = 1 - tileX % 1
        return (tileSize * relative).toInt()
    }

    private fun calculateBottomOffset(rect: BoundingBox, zoom: Int, tileSize: Int): Int {
        val tileY = latToTile(rect.latSouth, zoom)
        val relative = 1 - tileY % 1
        return (tileSize * relative).toInt()
    }

    private fun lonToTile(lon: Double, zoom: Int) = (lon + 180) / 360 * (1 shl zoom)

    private fun latToTile(lat: Double, zoom: Int): Double {
        return (1 shl zoom) * (1 - (Math.log(Math.tan(Math.toRadians(lat)) + 1 / (Math.cos(Math.toRadians(lat))))) / Math.PI) / 2
    }
}