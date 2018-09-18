package com.devindi.wallpaper.model.map.source

import timber.log.Timber

private val baseUrls = arrayOf("http://a.tile.stamen.com")

fun createToner() = StamenTileSource("Toner", "toner")
fun createWatercolor() = StamenTileSource("Watercolor", "watercolor")
fun createTerrain() = StamenTileSource("Terrain", "terrain")

class StamenTileSource(name: String, private val map: String) : BaseTileSource(name, baseUrls) {
    override fun getTileUrl(x: Int, y: Int, z: Int): String {
        val url = "$baseUrl/$map/$z/$x/$y.png"
        Timber.d("Tile url = $url")
        return url
    }
}
