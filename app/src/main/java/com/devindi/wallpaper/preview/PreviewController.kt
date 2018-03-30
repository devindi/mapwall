package com.devindi.wallpaper.preview

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.devindi.wallpaper.R
import org.osmdroid.tileprovider.tilesource.ThunderforestTileSource
import org.osmdroid.views.MapView

class PreviewController: Controller() {

    private lateinit var map:MapView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.preview_screen, container, false)
        map = view.findViewById(R.id.map_view)
        map.setTileSource(ThunderforestTileSource(container.context, ThunderforestTileSource.SPINAL_MAP))
        return view
    }

    override fun onActivityResumed(activity: Activity) {
        super.onActivityResumed(activity)
        map.onResume()
    }

    override fun onActivityPaused(activity: Activity) {
        super.onActivityPaused(activity)
        map.onPause()
    }
}