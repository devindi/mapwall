package com.devindi.wallpaper.model.map.source

import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase

class CompositeSource(private val sources: List<OnlineTileSourceBase>) : OnlineTileSourceBase("composite", 0, 17, 256, ".png", emptyArray()) {

    override fun getTileURLString(pMapTileIndex: Long): String {
        val source = sources[random.nextInt(sources.size)]
        return source.getTileURLString(pMapTileIndex)
    }
}
