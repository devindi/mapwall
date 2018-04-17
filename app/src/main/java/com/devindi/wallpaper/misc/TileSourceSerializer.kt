package com.devindi.wallpaper.misc

import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.tileprovider.tilesource.TileSourceFactory

class TileSourceSerializer {
    fun serialize(sourceBase: OnlineTileSourceBase): String {
        return "DEFAULT"
    }

    fun deserialize(raw: String): OnlineTileSourceBase {
        return TileSourceFactory.DEFAULT_TILE_SOURCE
    }
}