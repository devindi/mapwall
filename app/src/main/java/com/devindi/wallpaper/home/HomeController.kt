package com.devindi.wallpaper.home

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.archlifecycle.LifecycleController
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.bluelinelabs.conductor.changehandler.TransitionChangeHandlerCompat
import com.devindi.wallpaper.R
import com.devindi.wallpaper.about.AboutController
import com.devindi.wallpaper.misc.anim.FabToDialogTransitionChangeHandler
import com.devindi.wallpaper.misc.inject
import com.devindi.wallpaper.misc.viewModel
import com.devindi.wallpaper.model.SettingsRepo
import com.devindi.wallpaper.model.map.MapSource
import com.devindi.wallpaper.model.map.TileSourceFactory
import com.devindi.wallpaper.search.OnPlacePickedListener
import com.devindi.wallpaper.search.Place
import com.devindi.wallpaper.search.SearchChangeHandler
import com.devindi.wallpaper.search.SearchController
import com.devindi.wallpaper.settings.SettingsController
import com.devindi.wallpaper.source.MapSourceController
import com.squareup.picasso.Picasso
import org.osmdroid.config.IConfigurationProvider
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.TileSystem
import org.osmdroid.views.MapView
import timber.log.Timber
import java.io.File

private const val PLACE_ZOOM = 12.0

class HomeController : LifecycleController(), OnPlacePickedListener {

    private val settings: SettingsRepo by inject()
    private val osmConfig: IConfigurationProvider by inject()
    private val viewModel: HomeViewModel by viewModel()
    private val factory: TileSourceFactory by inject()

    private lateinit var map: MapView
    private lateinit var drawer: DrawerLayout
    private lateinit var navigation: NavigationView

    init {
        osmConfig.osmdroidBasePath = File(settings.getMapCachePath())
    }

    @SuppressLint("NewApi")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.home_screen, container, false)
        map = view.findViewById(R.id.map_view)
        map.isVerticalMapRepetitionEnabled = false
        map.setMultiTouchControls(true)
        map.setBuiltInZoomControls(false)
        map.setScrollableAreaLimitLatitude(TileSystem.MaxLatitude,-TileSystem.MaxLatitude, 0)

        drawer = view.findViewById(R.id.drawer)

        navigation = view.findViewById(R.id.navigation)
        Picasso.with(container.context)
                //todo place nice location and tile source (Maybe popular?)
                .load(Uri.parse("osm://mapnik?latNorth=85&latSouth=-85&lonWest=-170&lonEast=170&zoom=0"))
                .into(navigation.getHeaderView(0) as ImageView)
        navigation.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.settings_item -> {
                    drawer.closeDrawers()
                    router.pushController(RouterTransaction.with(SettingsController()))
                    return@setNavigationItemSelectedListener true
                }
                R.id.about_item -> {
                    drawer.closeDrawers()
                    router.pushController(RouterTransaction.with(AboutController()))
                    return@setNavigationItemSelectedListener true
                }
                else -> false
            }
        }

        view.findViewById<View>(R.id.button).setOnClickListener {
            viewModel.createWallpaper(map.boundingBox, map.zoomLevel)
        }

        view.findViewById<View>(R.id.menu_button).setOnClickListener {
            drawer.openDrawer(Gravity.START)
        }

        viewModel.currentTileSource.observe(this, Observer<MapSource> { t ->
            val tileSource = factory.getTileSource(t!!.id)
            map.setTileSource(tileSource)

            val metrics = DisplayMetrics()
            activity!!.windowManager.defaultDisplay.getMetrics(metrics)
            val screenHeight = metrics.heightPixels

            val minZoom = calculateMinZoom(screenHeight, tileSource.tileSizePixels)
            map.minZoomLevel = minZoom
            if (map.zoomLevelDouble < minZoom) {
                map.controller.setZoom(minZoom)
            }
        })

        view.findViewById<View>(R.id.search_edit_fake).setOnClickListener {
            val target = SearchController()
            target.targetController = this
            router.pushController(RouterTransaction
                    .with(target)
                    .pushChangeHandler(SearchChangeHandler(removesFromViewOnPush = false))
                    .popChangeHandler(SearchChangeHandler())
            )
        }

        view.findViewById<View>(R.id.btn_select_source).setOnClickListener {

            val pushHandler = TransitionChangeHandlerCompat(
                    FabToDialogTransitionChangeHandler(),
                    FadeChangeHandler(false))
            val popHandler = TransitionChangeHandlerCompat(
                    FabToDialogTransitionChangeHandler(),
                    FadeChangeHandler())

            router.pushController(RouterTransaction.with(MapSourceController())
                    .pushChangeHandler(pushHandler).popChangeHandler(popHandler))
        }
        return view
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        map.onResume()
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
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
        map.controller.setZoom(PLACE_ZOOM)
        map.controller.animateTo(GeoPoint(place.lat, place.lon))
    }

    private fun calculateMinZoom(height: Int, tileSize: Int): Double {
        return Math.log(height/tileSize.toDouble()) / Math.log(2.0)
    }
}
