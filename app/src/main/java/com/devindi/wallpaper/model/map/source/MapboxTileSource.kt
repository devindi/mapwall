package com.devindi.wallpaper.model.map.source

fun createMapBoxTerrain() = MapboxTileSource("Satellite", "satellite", "pk.eyJ1IjoiZGV2aW5kaSIsImEiOiJjamhpM3JoNzgwMjZ0MzZwNTNja3BreGk0In0.6sHXwbbheRdPyUXp5ucV5g")
fun createMapBoxComic() = MapboxTileSource("Comic", "comic", "pk.eyJ1IjoiZGV2aW5kaSIsImEiOiJjamhpM3JoNzgwMjZ0MzZwNTNja3BreGk0In0.6sHXwbbheRdPyUXp5ucV5g")
fun createMapBoxPirates() = MapboxTileSource("Pirates", "pirates", "pk.eyJ1IjoiZGV2aW5kaSIsImEiOiJjamhpM3JoNzgwMjZ0MzZwNTNja3BreGk0In0.6sHXwbbheRdPyUXp5ucV5g")
fun createMapBoxPencil() = MapboxTileSource("Pencil", "pencil", "pk.eyJ1IjoiZGV2aW5kaSIsImEiOiJjamhpM3JoNzgwMjZ0MzZwNTNja3BreGk0In0.6sHXwbbheRdPyUXp5ucV5g")

class MapboxTileSource(name: String, private val map: String, private val apiKey: String): BaseTileSource(name,  arrayOf("https://a.tiles.mapbox.com/v4")) {
    override fun getTileUrl(x: Int, y: Int, z: Int): String {
        return "$baseUrl/mapbox.$map/$z/$x/$y.png?access_token=$apiKey"
    }
}