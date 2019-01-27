package com.devindi.wallpaper.model.map

import android.graphics.Bitmap
import android.graphics.Canvas
import com.devindi.wallpaper.misc.RectD
import com.google.firebase.perf.metrics.AddTrace
import org.osmdroid.util.MapTileIndex
import timber.log.Timber

/**
 * Generates bitmap by params (source, center coordinates, zoom, size)
 */
class MapImageGenerator(
    private val tileProvider: SyncMapTileProvider,
    private val sourceFactory: TileSourceFactory,
    private val tileUtils: TileUtils
) {

    fun generate(mapSourceId: String, zoom: Int, centerLat: Double, centerLong: Double, width: Int, height: Int): Bitmap {
        Timber.d("Generate bitmap: $centerLat, $centerLong, $width, $height")
        val tileSource = sourceFactory.getTileSource(mapSourceId)
        val tilesRect = tileUtils.getTilesRect(centerLat, centerLong, width, height, tileSource.tileSizePixels, zoom)
        return generate(mapSourceId, tilesRect, zoom)
    }

    @AddTrace(name = "bitmap generation")
    fun generate(sourceId: String, tiles: RectD, zoom: Int): Bitmap {
        Timber.d("Generating image for %s", tiles)
        val tileSource = sourceFactory.getTileSource(sourceId)

        val maxIndex = Math.pow(2.0, zoom.toDouble()).toInt() - 1

        val startX = Math.floor(tiles.left).toInt()
        val endX = Math.min(maxIndex, Math.ceil(tiles.right).toInt())

        val xRange = TilesRange(startX, endX, zoom)

        val startY = Math.floor(tiles.top).toInt()
        val endY = Math.min(maxIndex, Math.ceil(tiles.bottom).toInt())

        val yRange = TilesRange(startY, endY, zoom)

        val tileSize = tileSource.tileSizePixels
        val left = calculateLeftOffset(tiles.left, tileSize)
        val top = calculateTopOffset(tiles.top, tileSize)
        val right = calculateRightOffset(tiles.right, tileSize)
        val bottom = calculateBottomOffset(tiles.bottom, tileSize)

        val target = Bitmap.createBitmap(
            xRange.size() * tileSize - left - right,
            yRange.size() * tileSize - top - bottom,
            Bitmap.Config.RGB_565)
        Timber.d("target size: ${target.height} ${target.width}")
        val canvas = Canvas(target)
        xRange.forEachIndexed { xIndex, xTile ->
            yRange.forEachIndexed { yIndex, yTile ->
                val pxLeft = xIndex * tileSize.toFloat() - left
                val pxTop = yIndex * tileSize.toFloat() - top
                val tileBitmap = tileProvider.getTile(tileSource, MapTileIndex.getTileIndex(zoom, xTile, yTile))
                Timber.d("Drawing tile new %s / %s / %s at %s %s",
                    zoom,
                    xTile,
                    yTile,
                    pxLeft,
                    pxTop)
                canvas.drawBitmap(tileBitmap, pxLeft, pxTop, null)
            }
        }
        return target
    }

    private fun calculateLeftOffset(tileX: Double, tileSize: Int): Int {
        val relative = tileX % 1
        return (tileSize * relative).toInt()
    }

    private fun calculateTopOffset(tileY: Double, tileSize: Int): Int {
        val relative = tileY % 1
        return (tileSize * relative).toInt()
    }

    private fun calculateRightOffset(tileX: Double, tileSize: Int): Int {
        val relative = 1 - tileX % 1
        return (tileSize * relative).toInt()
    }

    private fun calculateBottomOffset(tileY: Double, tileSize: Int): Int {
        val relative = 1 - tileY % 1
        return (tileSize * relative).toInt()
    }
}
