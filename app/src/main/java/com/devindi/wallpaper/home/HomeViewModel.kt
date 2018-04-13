package com.devindi.wallpaper.home

import android.arch.lifecycle.ViewModel
import com.devindi.wallpaper.PARAM_TILE_SOURCE
import com.devindi.wallpaper.misc.get
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox

class HomeViewModel(private val handler: WallpaperHandler): ViewModel() {

    private var factory: WallpaperFactory = get { mapOf(PARAM_TILE_SOURCE to TileSourceFactory.DEFAULT_TILE_SOURCE) }

    var currentTileSource: OnlineTileSourceBase = TileSourceFactory.DEFAULT_TILE_SOURCE
        set(value) {
            factory = get { mapOf(PARAM_TILE_SOURCE to value) }
            field = value
        }

    fun createWallpaper(boundingBox: BoundingBox, zoomLevel: Int) {
        Thread({
            val wallpaper = factory.createWallpaper(boundingBox, zoomLevel)
            handler.handle(wallpaper, Target.BOTH)
        }).start()
    }
}