package com.devindi.wallpaper.home

import android.arch.lifecycle.ViewModel
import com.devindi.wallpaper.misc.ReportManager
import com.devindi.wallpaper.model.SettingsRepo
import com.devindi.wallpaper.model.analytics.CreateWallpaperEvent
import com.devindi.wallpaper.model.history.HistoryManager
import com.devindi.wallpaper.model.history.WallpaperEntry
import com.devindi.wallpaper.model.map.MapImageGenerator
import com.devindi.wallpaper.settings.model.HeightField
import com.devindi.wallpaper.settings.model.WallpaperMode
import com.devindi.wallpaper.settings.model.WidthField
import com.google.firebase.perf.FirebasePerformance
import org.osmdroid.util.GeoPoint
import java.util.Calendar

class HomeViewModel(
    private val imageGenerator: MapImageGenerator,
    private val handler: ImageHandler,
    private val reportManager: ReportManager,
    settings: SettingsRepo,
    private val historyManager: HistoryManager,
    private val widthField: WidthField,
    private val heightField: HeightField
) : ViewModel() {

    var currentTileSource = settings.currentMapSource()

    fun createWallpaper(centerPoint: GeoPoint, zoomLevel: Int) {
        Thread {
            val myTrace = FirebasePerformance.getInstance().newTrace("wallpaper gen trace")
            myTrace.start()
            val width = widthField.get()
            val height = heightField.get()
            reportManager.reportEvent(CreateWallpaperEvent(
                currentTileSource.value!!.id,
                centerPoint.latitude,
                centerPoint.longitude,
                zoomLevel,
                WallpaperMode.BOTH) // TODO track target
            )
            val wallpaper = imageGenerator.generate(
                currentTileSource.value!!.id,
                zoomLevel,
                centerPoint.latitude,
                centerPoint.longitude,
                width,
                height)
            myTrace.stop()
            historyManager.addEntry(
                WallpaperEntry(
                    currentTileSource.value!!.id,
                    centerPoint.latitude,
                    centerPoint.longitude,
                    zoomLevel.toDouble(),
                    width,
                    height,
                    Calendar.getInstance()))
            handler.handle(wallpaper)
        }.start()
    }
}
