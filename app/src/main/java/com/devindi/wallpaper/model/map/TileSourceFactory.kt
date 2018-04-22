package com.devindi.wallpaper.model.map

import android.content.Context
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.tileprovider.tilesource.ThunderforestTileSource
import org.osmdroid.tileprovider.tilesource.TileSourceFactory as Impl

class TileSourceFactory(private val context: Context) {

    private val impl = mapOf<String, OnlineTileSourceBase>(
            "mapnik" to Impl.MAPNIK,
            "public_transport" to Impl.PUBLIC_TRANSPORT,
            "cloudmate" to Impl.CLOUDMADESTANDARDTILES,
            "thunder.spinal" to ThunderforestTileSource(context, ThunderforestTileSource.SPINAL_MAP),
            "thunder.transport_dark" to ThunderforestTileSource(context, ThunderforestTileSource.TRANSPORT_DARK),
            "thunder.outdoor" to ThunderforestTileSource(context, ThunderforestTileSource.OUTDOORS),
            "thunder.cycle" to ThunderforestTileSource(context, ThunderforestTileSource.CYCLE),
            "thunder.transport" to ThunderforestTileSource(context, ThunderforestTileSource.TRANSPORT),
            "thunder.landscape" to ThunderforestTileSource(context, ThunderforestTileSource.LANDSCAPE),
            "thunder.pioneer" to ThunderforestTileSource(context, ThunderforestTileSource.PIONEER),
            "thunder.atlas" to ThunderforestTileSource(context, ThunderforestTileSource.MOBILE_ATLAS),
            "thunder.neighbourhood" to ThunderforestTileSource(context, ThunderforestTileSource.NEIGHBOURHOOD)
    )

    fun getTileSource(id: String): OnlineTileSourceBase {
        return impl.getOrDefault(id, Impl.DEFAULT_TILE_SOURCE)
    }
}