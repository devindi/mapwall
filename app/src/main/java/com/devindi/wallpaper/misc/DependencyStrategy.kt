package com.devindi.wallpaper.misc

import com.devindi.wallpaper.model.map.MapAreaManager
import com.devindi.wallpaper.model.map.TileRequestHandler
import com.devindi.wallpaper.model.map.mapModule
import com.squareup.picasso.Picasso
import org.koin.standalone.KoinComponent
import org.koin.standalone.StandAloneContext
import org.koin.standalone.get
import timber.log.Timber

class DependencyStrategy: KoinComponent {

    /**
     * Loads [mapModule] to koin and re-creates Picasso to handle internal links to map images
     */
    fun initMapModule() {
        StandAloneContext.loadKoinModules(mapModule)
        val manager: MapAreaManager = get()

        val picasso = Picasso.Builder(get())
                .loggingEnabled(true)
                .addRequestHandler(TileRequestHandler(manager))
                .listener { _, uri, exception -> Timber.e(exception, "Failed to load $uri") }
                .build()
        Picasso.setSingletonInstance(picasso)
    }
}
