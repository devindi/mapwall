package com.devindi.wallpaper.misc

import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase

class Config(defaultSource: OnlineTileSourceBase) {
    val defaultMapSourceData = ConstLiveData(defaultSource)

    private var default = MapSource("DEFAULT", "Mapnik")

    val availableSources = ConstLiveData(listOf(default, default, default, default, default, default, default, default, default))
}
