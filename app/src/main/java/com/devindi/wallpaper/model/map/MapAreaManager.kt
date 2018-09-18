package com.devindi.wallpaper.model.map

import android.graphics.Bitmap
import android.graphics.Canvas
import org.osmdroid.tileprovider.cachemanager.CacheManager
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.MapTileIndex
import timber.log.Timber

private const val HALF_CIRCLE_DEGREES = 180
private const val CIRCLE_DEGREES = 360

class MapAreaManager(
    private val tileProvider: SyncMapTileProvider,
    private val sourceFactory: TileSourceFactory
) {

    fun generateBitmap(sourceId: String, area: BoundingBox, zoom: Int): Bitmap {
        val tileSource = sourceFactory.getTileSource(sourceId)
        var tilesCoverage = CacheManager.getTilesCoverage(area, zoom)
        tilesCoverage = tilesCoverage.sortedWith(
            compareBy({ MapTileIndex.getX(it) }, { MapTileIndex.getY(it) })
        )
        val startX = MapTileIndex.getX(tilesCoverage[0])
        val tilesY = tilesCoverage
            .filter { MapTileIndex.getX(it) == startX }
            .size
        val tilesX = tilesCoverage.size / tilesY
        val tileSize = tileSource.tileSizePixels
        val left = calculateLeftOffset(area, zoom, tileSize)
        val top = calculateTopOffset(area, zoom, tileSize)
        val right = calculateRightOffset(area, zoom, tileSize)
        val bottom = calculateBottomOffset(area, zoom, tileSize)
        val target = Bitmap.createBitmap(tilesX * tileSize - left - right,
            tilesY * tileSize - top - bottom, Bitmap.Config.RGB_565)
        val canvas = Canvas(target)
        for (i in 0 until tilesCoverage.size) {
            val pxLeft = (i / tilesY) * tileSize.toFloat() - left
            val pxTop = (i % tilesY) * tileSize.toFloat() - top
            val bitmap = tileProvider.getTile(tileSource, tilesCoverage[i])
            Timber.d("Drawing tile %s / %s / %s at %s %s",
                MapTileIndex.getZoom(tilesCoverage[i]),
                MapTileIndex.getX(tilesCoverage[i]),
                MapTileIndex.getY(tilesCoverage[i]),
                pxLeft,
                pxTop)
            canvas.drawBitmap(bitmap, pxLeft, pxTop, null)
        }
        return target
    }

    fun generateBitmap(
        sourceId: String,
        north: Double,
        east: Double,
        south: Double,
        west: Double,
        zoom: Int
    ): Bitmap {
        return generateBitmap(sourceId, BoundingBox(north, east, south, west), zoom)
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

    /* ktlint-disable max-line-length */
    // Here is some math magic). Those calculations taken from OSM wiki
    // https://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
    private fun lonToTile(lon: Double, zoom: Int) = (lon + HALF_CIRCLE_DEGREES) / CIRCLE_DEGREES * (1 shl zoom)

    private fun latToTile(lat: Double, zoom: Int): Double = (1 shl zoom) * (1 - (Math.log(Math.tan(Math.toRadians(lat)) + 1 / (Math.cos(Math.toRadians(lat))))) / Math.PI) / 2
    /* ktlint-enable max-line-length */
}
