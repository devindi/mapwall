package com.devindi.wallpaper.model.map

import com.squareup.picasso.Picasso
import com.squareup.picasso.Request
import com.squareup.picasso.RequestHandler
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import timber.log.Timber

class TileRequestHandler : RequestHandler(), KoinComponent {

    private val mapAreaManager: MapImageGenerator by inject()

    override fun canHandleRequest(data: Request): Boolean {
        return data.uri.scheme == "osm"
    }

    override fun load(request: Request, networkPolicy: Int): Result {
        Timber.d("Loading bitmap for url: ${request.uri}")
        val sourceId = request.uri.host
        val zoom = request.uri.getQueryParameter("zoom").toInt()
        val bitmap = mapAreaManager.generate(sourceId, 1, 0.0, 0.0, 50, 50)
        return Result(bitmap, Picasso.LoadedFrom.DISK)
    }
}
