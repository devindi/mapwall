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

import com.devindi.wallpaper.misc.inject
import org.osmdroid.config.IConfigurationProvider
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.TileSystem
import org.osmdroid.views.MapView
import java.io.File

class HomeController : Controller() {

    private lateinit var map:MapView

    private val settings: SettingsRepo by inject()
    private val osmConfig: IConfigurationProvider by inject()
    private val viewModel: HomeViewModel by inject()

    init {
        osmConfig.osmdroidBasePath = File(settings.getMapCachePath())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.home_screen, container, false)
        map = view.findViewById(R.id.map_view)
        return view
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        val tileSource = viewModel.currentTileSource

        view.findViewById<View>(R.id.button).setOnClickListener {
            viewModel.createWallpaper(map.boundingBox, map.zoomLevel)
        }

        map.isVerticalMapRepetitionEnabled = false
        map.setMultiTouchControls(true)
        map.setBuiltInZoomControls(false)
        map.setScrollableAreaLimitLatitude(TileSystem.MaxLatitude,-TileSystem.MaxLatitude, 0)
        map.setTileSource(tileSource)

        val metrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(metrics)
        val screenHeight = metrics.heightPixels
        val minZoom = calculateMinZoom(screenHeight, tileSource.tileSizePixels)
        map.minZoomLevel = minZoom
        if (map.zoomLevelDouble < minZoom) {
            map.controller.setZoom(minZoom)
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
        map.onResume()
    }

    override fun onActivityPaused(activity: Activity) {
        super.onActivityPaused(activity)
        map.onPause()
    }

    private fun calculateMinZoom(height: Int, tileSize: Int): Double {
        return Math.log(height/tileSize.toDouble()) / Math.log(2.0)
    }
}