package com.devindi.wallpaper.home

import android.arch.lifecycle.ViewModel
import com.devindi.wallpaper.misc.ReportManager
import com.devindi.wallpaper.model.SettingsRepo
import com.devindi.wallpaper.model.analytics.CreateWallpaperEvent
import com.devindi.wallpaper.model.history.HistoryManager
import com.devindi.wallpaper.model.history.WallpaperEntry
import com.devindi.wallpaper.model.map.MapImageGenerator
import com.devindi.wallpaper.settings.model.DIMENSION_HEIGHT
import com.devindi.wallpaper.settings.model.DIMENSION_WIDTH
import com.devindi.wallpaper.settings.model.SettingsManager
import com.google.firebase.perf.FirebasePerformance
import org.osmdroid.util.GeoPoint
import java.util.Calendar

class HomeViewModel(
    private val imageGenerator: MapImageGenerator,
    private val handler: WallpaperHandler,
    private val reportManager: ReportManager,
    settings: SettingsRepo,
    private val settingsManager: SettingsManager,
    private val historyManager: HistoryManager
) : ViewModel() {

    var currentTileSource = settings.currentMapSource()

    fun createWallpaper(centerPoint: GeoPoint, zoomLevel: Int) {
        Thread {
            val myTrace = FirebasePerformance.getInstance().newTrace("wallpaper gen trace")
            myTrace.start()
            val target = Target.BOTH
            val width = settingsManager.getIntField(DIMENSION_WIDTH).get()
            val height = settingsManager.getIntField(DIMENSION_HEIGHT).get()
            reportManager.reportEvent(CreateWallpaperEvent(
                currentTileSource.value!!.id,
                centerPoint.latitude,
                centerPoint.longitude,
                zoomLevel,
                target)
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
                    width,
                    height,
                    Calendar.getInstance()))
            handler.handle(wallpaper, target)
        }.start()
    }
}
