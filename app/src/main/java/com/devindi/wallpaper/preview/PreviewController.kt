package com.devindi.wallpaper.preview

import android.app.Activity
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
import org.osmdroid.views.MapView

class PreviewController : Controller() {

    private lateinit var map:MapView
    private lateinit var consumer: WallpaperConsumer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.preview_screen, container, false)
        map = view.findViewById(R.id.map_view)
        map.isVerticalMapRepetitionEnabled = false
        map.isHorizontalMapRepetitionEnabled = false
        map.setMultiTouchControls(true)
        map.setBuiltInZoomControls(false)
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

    override fun onActivityResumed(activity: Activity) {
        super.onActivityResumed(activity)
        consumer = WallpaperSaver(activity.getExternalFilesDir(null))
        map.onResume()
    }

    override fun onActivityPaused(activity: Activity) {
        super.onActivityPaused(activity)
        map.onPause()
    }

    private fun createFactory(tileSource: OnlineTileSourceBase):WallpaperFactory {
        val cache = SqlTileWriter()
        return WallpaperFactory(CacheManager(tileSource, cache, tileSource.minimumZoomLevel, tileSource.maximumZoomLevel), tileSource, cache)
    }
}