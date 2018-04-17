package com.devindi.wallpaper.misc

import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import timber.log.Timber

class ConfigManager {

    val config: Config by lazy {
        Timber.d("Loading config")
        //todo implement
        Config(TileSourceFactory.DEFAULT_TILE_SOURCE)
    }
}