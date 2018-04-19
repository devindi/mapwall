package com.devindi.wallpaper.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.devindi.wallpaper.model.map.TileSourceSerializer
import com.devindi.wallpaper.model.storage.KeyValueStorage
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase

private const val OSM_CACHE_PATH = "osm"
private const val OSM_TILE_SOURCE = "tile_source"

class SettingsRepo(private val storage: KeyValueStorage, private val serializer: TileSourceSerializer) {

    private val currentMapSource = MutableLiveData<OnlineTileSourceBase>()

    init {
        val serialized = storage.read(OSM_TILE_SOURCE, String::class)
        if (serialized != null) {
            currentMapSource.value = serializer.deserialize(serialized)
        }
    }

    fun getMapCachePath(): String? {
        return storage.read(OSM_CACHE_PATH, String::class)
    }

    fun setMapCachePath(path: String) {
        storage.save(OSM_CACHE_PATH, path)
    }

    fun setCurrentMapSource(source: OnlineTileSourceBase) {
        currentMapSource.value = source
        val serialized = serializer.serialize(source)
        storage.save(OSM_TILE_SOURCE, serialized)
    }

    fun currentMapSource(): LiveData<OnlineTileSourceBase?>  = currentMapSource
}
