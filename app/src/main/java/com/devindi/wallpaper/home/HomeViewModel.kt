package com.devindi.wallpaper.home

import android.arch.lifecycle.ViewModel
import com.devindi.wallpaper.model.SettingsRepo
import com.devindi.wallpaper.model.map.MapAreaManager
import org.osmdroid.util.BoundingBox

class HomeViewModel(private val manager: MapAreaManager, private val handler: WallpaperHandler, settings: SettingsRepo): ViewModel() {

    var currentTileSource = settings.currentMapSource()

    fun createWallpaper(boundingBox: BoundingBox, zoomLevel: Int) {
        Thread({
            val wallpaper = manager.generateBitmap(currentTileSource.value!!.id, boundingBox, zoomLevel)
            handler.handle(wallpaper, Target.BOTH)
        }).start()
    }
}