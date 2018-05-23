package com.devindi.wallpaper.model.config

import com.devindi.wallpaper.misc.ConstLiveData
import com.devindi.wallpaper.model.map.MapSource

class Config {
    private var default = MapSource("mapnik", "Mapnik")

    val defaultMapSourceData = ConstLiveData(default)

    val availableSources = listOf(
            default,
            MapSource("thunder.spinal", "Spinal"),
            MapSource("thunder.transport_dark", "Transport Dark"),
            MapSource("thunder.outdoor", "Outdoors"),
            MapSource("thunder.pioneer", "Pioneer"),
            MapSource("thunder.atlas", "Atlas"),
            MapSource("thunder.neighbourhood", "NEIGHBOURHOOD"),
            MapSource("stamen.toner", "Toner"),
            MapSource("stamen.watercolor", "Water color"),
            MapSource("stamen.terrain", "Terrain"),
            MapSource("mapbox.satellite", "Satellite"),
            MapSource( "mapbox.pencil", "Pencil"),
            MapSource("mapbox.pirates", "Pirates"),
            MapSource("mapbox.comic", "Comic")
    )
}
