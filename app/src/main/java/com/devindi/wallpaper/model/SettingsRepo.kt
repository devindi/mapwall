package com.devindi.wallpaper.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.devindi.wallpaper.model.map.MapSource
import com.devindi.wallpaper.model.map.TileSourceSerializer
import com.devindi.wallpaper.model.storage.KeyValueStorage
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase

private const val OSM_CACHE_PATH = "osm"
private const val OSM_TILE_SOURCE = "tile_source"

class SettingsRepo(private val storage: KeyValueStorage) {

    private val currentMapSource = MutableLiveData<MapSource>()

    init {
        val serialized = storage.read(OSM_TILE_SOURCE, String::class)
        if (serialized != null) {
            currentMapSource.value = MapSource("id", "title")
        }
    }

    fun getMapCachePath(): String? {
        return storage.read(OSM_CACHE_PATH, String::class)
    }

    fun setMapCachePath(path: String) {
        storage.save(OSM_CACHE_PATH, path)
    }

    fun setCurrentMapSource(source: MapSource) {
        currentMapSource.value = source
        storage.save(OSM_TILE_SOURCE, "id")
    }

    fun currentMapSource(): LiveData<MapSource?>  = currentMapSource
}
