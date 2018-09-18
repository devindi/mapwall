package com.devindi.wallpaper.model.map.source

/* ktlint-disable max-line-length */
// TODO inject correct key during assemble with gradle like other keys
private const val MAPBOX_KEY = "pk.eyJ1IjoiZGV2aW5kaSIsImEiOiJjamhpM3JoNzgwMjZ0MzZwNTNja3BreGk0In0.6sHXwbbheRdPyUXp5ucV5g"
/* ktlint-enable max-line-length */

fun createMapBoxTerrain() = MapboxTileSource("Satellite", "satellite", MAPBOX_KEY)
fun createMapBoxComic() = MapboxTileSource("Comic", "comic", MAPBOX_KEY)
fun createMapBoxPirates() = MapboxTileSource("Pirates", "pirates", MAPBOX_KEY)
fun createMapBoxPencil() = MapboxTileSource("Pencil", "pencil", MAPBOX_KEY)

class MapboxTileSource(
    name: String,
    private val map: String,
    private val apiKey: String
) : BaseTileSource(name, arrayOf("https://a.tiles.mapbox.com/v4")) {
    override fun getTileUrl(x: Int, y: Int, z: Int): String {
        return "$baseUrl/mapbox.$map/$z/$x/$y.png?access_token=$apiKey"
    }
}