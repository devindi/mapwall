package com.devindi.wallpaper.model.config

import com.devindi.wallpaper.misc.ConstLiveData
import com.devindi.wallpaper.model.map.MapSource

class Config {
    private var default = MapSource("mapnik", "Mapnik")

    val defaultMapSourceData = ConstLiveData(default)

    val availableSources = listOf(
            default,
            MapSource("public_transport", "Public Transport"),
            MapSource("cloudmate", "CloudMade"),
            MapSource("thunder.spinal", "Spinal"),
            MapSource("thunder.transport_dark", "Transport Dark"),
            MapSource("thunder.outdoor", "Outdoors"),
            MapSource("thunder.cycle", "OpenCycleMap"),
            MapSource("thunder.transport", "Transport"),
            MapSource("thunder.landscape", "Landscape"),
            MapSource("thunder.pioneer", "Pioneer"),
            MapSource("thunder.atlas", "Atlas"),
            MapSource("thunder.neighbourhood", "NEIGHBOURHOOD"))
}
