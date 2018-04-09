package com.devindi.wallpaper.home

import android.app.Activity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.devindi.wallpaper.R
import com.devindi.wallpaper.misc.SettingsRepo
import com.devindi.wallpaper.misc.WallpaperConsumer
import com.devindi.wallpaper.misc.inject
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.cachemanager.CacheManager
import org.osmdroid.tileprovider.modules.SqlTileWriter
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.TileSystem
import org.osmdroid.views.MapView
import java.io.File

class HomeController : Controller() {

    private lateinit var map:MapView
    private lateinit var consumer: WallpaperConsumer

    private val settings: SettingsRepo by inject()

    init {
        val config = Configuration.getInstance()
        config.osmdroidBasePath = File(settings.getMapCachePath())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.home_screen, container, false)
        map = view.findViewById(R.id.map_view)
        return view
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        showMap()
    }

    private fun showMap() {
        map.run {
            val tileSource = TileSourceFactory.DEFAULT_TILE_SOURCE
            val factory = createFactory(tileSource)
            val btn = view!!.findViewById<View>(R.id.button)
            btn.setOnClickListener {
                Thread(Runnable {
                    consumer.apply(factory.createWallpaper(map.boundingBox, map.zoomLevel))
                }).start()
            }

            isVerticalMapRepetitionEnabled = false
            setMultiTouchControls(true)
            setBuiltInZoomControls(false)
            setScrollableAreaLimitLatitude(TileSystem.MaxLatitude,-TileSystem.MaxLatitude, 0)
            setTileSource(tileSource)

            val metrics = DisplayMetrics()
            activity!!.windowManager.defaultDisplay.getMetrics(metrics)
            val screenHeight = metrics.heightPixels
            val minZoom = calculateMinZoom(screenHeight, TileSourceFactory.DEFAULT_TILE_SOURCE.tileSizePixels)
            minZoomLevel = minZoom
            if (zoomLevelDouble < minZoom) {
                controller.setZoom(minZoom)
            }
            onResume()
        }
    }

    override fun onSaveViewState(view: View, outState: Bundle) {
        super.onSaveViewState(view, outState)
        map.let {
            outState.putDouble("lat", it.mapCenter.latitude)
            outState.putDouble("lon", it.mapCenter.longitude)
            outState.putDouble("zoom", it.zoomLevelDouble)
        }

    }

    override fun onRestoreViewState(view: View, savedViewState: Bundle) {
        super.onRestoreViewState(view, savedViewState)
        map.let {
            it.controller.setCenter(GeoPoint(savedViewState.getDouble("lat"), savedViewState.getDouble("lon")))
            it.controller.setZoom(savedViewState.getDouble("zoom"))
        }
    }

    override fun onActivityResumed(activity: Activity) {
        super.onActivityResumed(activity)
        showMap()
        consumer = WallpaperSaver(activity.getExternalFilesDir(null))
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