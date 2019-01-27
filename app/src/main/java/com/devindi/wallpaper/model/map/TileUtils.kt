package com.devindi.wallpaper.model.map

import com.devindi.wallpaper.misc.RectD
import timber.log.Timber

class TileUtils {

    fun getTilesRect(
        centerLat: Double,
        centerLon: Double,
        width: Int,
        height: Int,
        tileSize: Int,
        zoom: Int
    ): RectD {
        val centerTileY = latToTile(centerLat, zoom)
        val centerTileX = lonToTile(centerLon, zoom)
        val tileWidth = width / (2.0 * tileSize)
        var westTileX = centerTileX - tileWidth
        val eastTileX = centerTileX + tileWidth
        val tileHeight = height / (2.0 * tileSize)
        val northTileY = centerTileY - tileHeight
        val southTileY = centerTileY + tileHeight

        if (westTileX < 0) {
            val worldSize = Math.pow(2.0, zoom.toDouble()).toInt()
            westTileX += worldSize
        }
        Timber.d("Tiles rect: $westTileX - $eastTileX; $$northTileY - $southTileY")

        return RectD(westTileX, northTileY, eastTileX, southTileY)
    }

    /* ktlint-disable max-line-length */
    // Here is some math magic). Those calculations are taken from OSM wiki
    // https://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
    fun lonToTile(lon: Double, zoom: Int) = (lon + HALF_CIRCLE_DEGREES) / CIRCLE_DEGREES * (1 shl zoom)

    fun latToTile(lat: Double, zoom: Int): Double = (1 shl zoom) * (1 - (Math.log(Math.tan(Math.toRadians(lat)) + 1 / (Math.cos(Math.toRadians(lat))))) / Math.PI) / 2

    fun tileToLon(tileY: Double, zoom: Int): Double {
        val n = Math.PI - ((2 * Math.PI * tileY) / Math.pow(2.0, zoom.toDouble()))
        return HALF_CIRCLE_DEGREES / Math.PI * Math.atan(Math.sinh(n))
    }

    fun tileToLat(tileX: Double, zoom: Int): Double {
        return ((tileX / Math.pow(2.0, zoom.toDouble()) * CIRCLE_DEGREES) - HALF_CIRCLE_DEGREES)
    }

    companion object {
        private const val CIRCLE_DEGREES = 360
        private const val HALF_CIRCLE_DEGREES = 180
    }
}
