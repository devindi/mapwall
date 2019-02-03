package com.devindi.wallpaper.model.map

import com.squareup.picasso.Picasso
import com.squareup.picasso.Request
import com.squareup.picasso.RequestHandler
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import timber.log.Timber

fun buildTileRequest(
    mapSourceId: String = "mapnik",
    width: Int = 100,
    height: Int = 100,
    zoom: Int = 0,
    centerLat: Double = 0.0,
    centerLon: Double = 0.0
): String {
    return "osm://$mapSourceId?" +
        "width=$width&" +
        "height=$height&" +
        "zoom=$zoom&" +
        "centerLat=$centerLat&" +
        "centerLon=$centerLon"
}

class TileRequestHandler : RequestHandler(), KoinComponent {

    private val mapAreaManager: MapImageGenerator by inject()

    override fun canHandleRequest(data: Request): Boolean {
        return data.uri.scheme == "osm"
    }

    override fun load(request: Request, networkPolicy: Int): Result {
        Timber.d("Loading bitmap for url: ${request.uri}")
        val sourceId = request.uri.host
        val zoom = request.uri.getQueryParameter("zoom").toInt()
        val width = request.uri.getQueryParameter("width").toInt()
        val height = request.uri.getQueryParameter("height").toInt()
        val centerLat = request.uri.getQueryParameter("centerLat").toDouble()
        val centerLon = request.uri.getQueryParameter("centerLon").toDouble()

        val bitmap = mapAreaManager.generate(sourceId, zoom, centerLat, centerLon, width, height)
        return Result(bitmap, Picasso.LoadedFrom.DISK)
    }
}
