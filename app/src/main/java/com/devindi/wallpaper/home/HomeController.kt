package com.devindi.wallpaper.home

import android.app.Activity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.devindi.wallpaper.R
import com.devindi.wallpaper.misc.WallpaperConsumer
import org.osmdroid.tileprovider.cachemanager.CacheManager
import org.osmdroid.tileprovider.modules.SqlTileWriter
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.TileSystem
import org.osmdroid.views.MapView

class HomeController : Controller() {

    private lateinit var map:MapView
    private lateinit var consumer: WallpaperConsumer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.preview_screen, container, false)
        map = view.findViewById(R.id.map_view)
        map.isVerticalMapRepetitionEnabled = false
        map.setMultiTouchControls(true)
        map.setBuiltInZoomControls(false)
        map.setScrollableAreaLimitLatitude(TileSystem.MaxLatitude,-TileSystem.MaxLatitude, 0)
        val tileSource = TileSourceFactory.DEFAULT_TILE_SOURCE
        val factory = createFactory(tileSource)
        map.setTileSource(tileSource)
        val btn = view.findViewById<View>(R.id.button)
        btn.setOnClickListener {
            Thread(Runnable {
                consumer.apply(factory.createWallpaper(map.boundingBox, map.zoomLevel))
            }).start()
        }
        return view
    }

    override fun onSaveViewState(view: View, outState: Bundle) {
        super.onSaveViewState(view, outState)
        outState.putDouble("lat", map.mapCenter.latitude)
        outState.putDouble("lon", map.mapCenter.longitude)
        outState.putDouble("zoom", map.zoomLevelDouble)
    }

    override fun onRestoreViewState(view: View, savedViewState: Bundle) {
        super.onRestoreViewState(view, savedViewState)
        map.controller.setCenter(GeoPoint(savedViewState.getDouble("lat"), savedViewState.getDouble("lon")))
        map.controller.setZoom(savedViewState.getDouble("zoom"))
    }

    override fun onActivityResumed(activity: Activity) {
        super.onActivityResumed(activity)
        consumer = WallpaperSaver(activity.getExternalFilesDir(null))
        val metrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metrics)
        val screenHeight = metrics.heightPixels
        val minZoom = calculateMinZoom(screenHeight, TileSourceFactory.DEFAULT_TILE_SOURCE.tileSizePixels)
        map.minZoomLevel = minZoom
        if (map.zoomLevelDouble < minZoom) {
            map.controller.setZoom(minZoom)
        }
        map.onResume()
        map.invalidate()
    }

    override fun onActivityPaused(activity: Activity) {
        super.onActivityPaused(activity)
        map.onPause()
    }

    private fun createFactory(tileSource: OnlineTileSourceBase):WallpaperFactory {
        val cache = SqlTileWriter()
        return WallpaperFactory(CacheManager(tileSource, cache, tileSource.minimumZoomLevel, tileSource.maximumZoomLevel), tileSource, cache)
    }

    private fun calculateMinZoom(height: Int, tileSize: Int): Double {
        return Math.log(height/tileSize.toDouble()) / Math.log(2.0)
    }
}