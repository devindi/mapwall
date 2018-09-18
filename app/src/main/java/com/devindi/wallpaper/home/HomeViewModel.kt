package com.devindi.wallpaper.home

import android.arch.lifecycle.ViewModel
import com.devindi.wallpaper.misc.ReportManager
import com.devindi.wallpaper.model.SettingsRepo
import com.devindi.wallpaper.model.analytics.CreateWallpaperEvent
import com.devindi.wallpaper.model.map.MapAreaManager
import org.osmdroid.util.BoundingBox

class HomeViewModel(
    private val manager: MapAreaManager,
    private val handler: WallpaperHandler,
    private val reportManager: ReportManager,
    settings: SettingsRepo
) : ViewModel() {

    var currentTileSource = settings.currentMapSource()

    fun createWallpaper(boundingBox: BoundingBox, zoomLevel: Int) {
        Thread({
            val target = Target.BOTH
            reportManager.reportEvent(CreateWallpaperEvent(
                currentTileSource.value!!.id,
                boundingBox.centerLatitude,
                boundingBox.centerLongitude,
                zoomLevel,
                target)
            )
            val wallpaper = manager.generateBitmap(
                currentTileSource.value!!.id,
                boundingBox,
                zoomLevel)
            handler.handle(wallpaper, target)
        }).start()
    }
}
