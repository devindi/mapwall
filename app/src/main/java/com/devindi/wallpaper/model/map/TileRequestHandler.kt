package com.devindi.wallpaper.model.map

import com.squareup.picasso.Picasso
import com.squareup.picasso.Request
import com.squareup.picasso.RequestHandler
import timber.log.Timber

class TileRequestHandler(private val mapAreaManager: MapAreaManager): RequestHandler() {

    override fun canHandleRequest(data: Request): Boolean {
        return data.uri.scheme == "osm"
    }

    override fun load(request: Request, networkPolicy: Int): Result {
        Timber.d("Loading bitmap for url: ${request.uri}")
        val sourceId = request.uri.host
        val north = request.uri.getQueryParameter("latNorth").toDouble()
        val south = request.uri.getQueryParameter("latSouth").toDouble()
        val west = request.uri.getQueryParameter("lonWest").toDouble()
        val east = request.uri.getQueryParameter("lonEast").toDouble()
        val zoom = request.uri.getQueryParameter("zoom").toInt()
        val bitmap = mapAreaManager.generateBitmap(sourceId, north, east, south, west, zoom)
        return Result(bitmap, Picasso.LoadedFrom.DISK)
    }
}
