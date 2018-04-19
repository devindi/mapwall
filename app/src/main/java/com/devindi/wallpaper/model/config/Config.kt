package com.devindi.wallpaper.model.config

import com.devindi.wallpaper.misc.ConstLiveData
import com.devindi.wallpaper.model.map.MapSource
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase

class Config(defaultSource: OnlineTileSourceBase) {
    val defaultMapSourceData = ConstLiveData(defaultSource)

    private var default = MapSource("DEFAULT", "Mapnik")

    val availableSources = ConstLiveData(listOf(default, default, default, default, default, default, default, default, default))
}
