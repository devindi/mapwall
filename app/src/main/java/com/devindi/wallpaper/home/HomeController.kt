package com.devindi.wallpaper.home

import android.app.Activity
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.archlifecycle.LifecycleController
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.bluelinelabs.conductor.changehandler.TransitionChangeHandler
import com.bluelinelabs.conductor.changehandler.TransitionChangeHandlerCompat
import com.bluelinelabs.conductor.changehandler.VerticalChangeHandler
import com.devindi.wallpaper.R
import com.devindi.wallpaper.misc.SettingsRepo

import com.devindi.wallpaper.misc.inject
import com.devindi.wallpaper.misc.viewModel
import com.devindi.wallpaper.search.OnPlacePickedListener
import com.devindi.wallpaper.search.Place
import com.devindi.wallpaper.search.SearchController
import com.devindi.wallpaper.search.SearchViewModel
import org.osmdroid.config.IConfigurationProvider
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.TileSystem
import org.osmdroid.views.MapView
import timber.log.Timber
import java.io.File

class HomeController : LifecycleController(), OnPlacePickedListener {

    private lateinit var map:MapView
    private var place: Place? = null

    private val settings: SettingsRepo by inject()
    private val osmConfig: IConfigurationProvider by inject()
    private val viewModel: HomeViewModel by viewModel()

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
        Timber.d("Attach")
        val tileSource = viewModel.currentTileSource

        view.findViewById<View>(R.id.button).setOnClickListener {
            viewModel.createWallpaper(map.boundingBox, map.zoomLevel)
        }

        view.findViewById<View>(R.id.search_edit_fake).setOnClickListener {
            val target = SearchController()
            target.targetController = this
            router.pushController(RouterTransaction.with(target))
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
        map.onResume()

        place?.let {
            map.controller.setZoom(12.0)
            map.controller.animateTo(GeoPoint(it.lat, it.lon))
            place = null
        }
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        Timber.d("detach")
        map.onPause()
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

    override fun onPlacePicked(place: Place) {
        Timber.d("Picked $place")
        this.place = place
    }

    private fun calculateMinZoom(height: Int, tileSize: Int): Double {
        return Math.log(height/tileSize.toDouble()) / Math.log(2.0)
    }
}