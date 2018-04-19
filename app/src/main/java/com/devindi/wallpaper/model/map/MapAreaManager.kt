package com.devindi.wallpaper.model.map

import android.graphics.Bitmap
import org.osmdroid.util.BoundingBox

class MapAreaManager {

    fun generateBitmap(source: MapSource, area: BoundingBox, zoom: Int): Bitmap {
        throw NotImplementedError()
    }

    fun generateBitmap(source: MapSource, north: Double, east: Double, south: Double, west: Double, zoom: Int): Bitmap {
        return generateBitmap(source, BoundingBox(north, east, south, west), zoom)
    }
}
