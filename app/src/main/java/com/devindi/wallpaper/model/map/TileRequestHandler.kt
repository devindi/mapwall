package com.devindi.wallpaper.model.map

import com.squareup.picasso.Picasso
import com.squareup.picasso.Request
import com.squareup.picasso.RequestHandler
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.TileSystem

class TileRequestHandler(private val mapAreaManager: MapAreaManager): RequestHandler() {

    override fun canHandleRequest(data: Request): Boolean {
        return data.uri.scheme == "osm"
    }

    override fun load(request: Request, networkPolicy: Int): Result {
        //todo extract params from request
        val bitmap = mapAreaManager.generateBitmap("DEFAULT", BoundingBox(TileSystem.MaxLatitude, TileSystem.MaxLongitude - 1, TileSystem.MinLatitude, TileSystem.MinLongitude + 1), 0)
        return Result(bitmap, Picasso.LoadedFrom.DISK)
    }
}
