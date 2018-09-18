package com.devindi.wallpaper.model.map

import android.content.Context
import com.devindi.wallpaper.model.map.source.CompositeSource
import com.devindi.wallpaper.model.map.source.createMapBoxComic
import com.devindi.wallpaper.model.map.source.createMapBoxPencil
import com.devindi.wallpaper.model.map.source.createMapBoxPirates
import com.devindi.wallpaper.model.map.source.createMapBoxTerrain
import com.devindi.wallpaper.model.map.source.createTerrain
import com.devindi.wallpaper.model.map.source.createToner
import com.devindi.wallpaper.model.map.source.createWatercolor
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.tileprovider.tilesource.ThunderforestTileSource
import org.osmdroid.tileprovider.tilesource.TileSourceFactory as Impl

class TileSourceFactory(private val context: Context) {

    private val impl = mapOf<String, OnlineTileSourceBase>(
        "mapnik" to Impl.MAPNIK,
        "public_transport" to Impl.PUBLIC_TRANSPORT,
        "cloudmate" to Impl.CLOUDMADESTANDARDTILES,
        "thunder.spinal" to ThunderforestTileSource(context, ThunderforestTileSource.SPINAL_MAP),
        "thunder.transport_dark" to
            ThunderforestTileSource(context, ThunderforestTileSource.TRANSPORT_DARK),
        "thunder.outdoor" to ThunderforestTileSource(context, ThunderforestTileSource.OUTDOORS),
        "thunder.cycle" to ThunderforestTileSource(context, ThunderforestTileSource.CYCLE),
        "thunder.transport" to ThunderforestTileSource(context, ThunderforestTileSource.TRANSPORT),
        "thunder.landscape" to ThunderforestTileSource(context, ThunderforestTileSource.LANDSCAPE),
        "thunder.pioneer" to ThunderforestTileSource(context, ThunderforestTileSource.PIONEER),
        "thunder.atlas" to ThunderforestTileSource(context, ThunderforestTileSource.MOBILE_ATLAS),
        "thunder.neighbourhood" to
            ThunderforestTileSource(context, ThunderforestTileSource.NEIGHBOURHOOD),
        "stamen.toner" to createToner(),
        "stamen.watercolor" to createWatercolor(),
        "stamen.terrain" to createTerrain(),
        "mapbox.satellite" to createMapBoxTerrain(),
        "mapbox.pencil" to createMapBoxPencil(),
        "mapbox.pirates" to createMapBoxPirates(),
        "mapbox.comic" to createMapBoxComic(),
        "composite" to CompositeSource(
            listOf(
                createMapBoxTerrain(),
                createWatercolor(),
                createToner(),
                ThunderforestTileSource(context, ThunderforestTileSource.PIONEER),
                createTerrain(),
                Impl.MAPNIK))
    )

    fun getTileSource(id: String): OnlineTileSourceBase {
        val tileSourceBase = impl[id]
        if (tileSourceBase != null) {
            return tileSourceBase
        }
        return Impl.DEFAULT_TILE_SOURCE
    }
}
