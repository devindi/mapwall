package com.devindi.wallpaper.model.map.source

import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.util.MapTileIndex
import timber.log.Timber

abstract class BaseTileSource(
    name: String,
    baseUrl: Array<String>
) : OnlineTileSourceBase(name, 0, 17, 256, ".png", baseUrl) {
    final override fun getTileURLString(pMapTileIndex: Long): String {
        val url = getTileUrl(
            MapTileIndex.getX(pMapTileIndex),
            MapTileIndex.getY(pMapTileIndex),
            MapTileIndex.getZoom(pMapTileIndex)
        )
        Timber.d("Url = $url")
        return url
    }

    abstract fun getTileUrl(x: Int, y: Int, z: Int): String
}