package com.devindi.wallpaper.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devindi.wallpaper.model.config.ConfigManager
import com.devindi.wallpaper.model.map.MapSource
import com.devindi.wallpaper.model.storage.KeyValueStorage

private const val OSM_CACHE_PATH = "osm"
private const val OSM_TILE_SOURCE = "tile_source"

class SettingsRepo(private val storage: KeyValueStorage, config: ConfigManager) {

    private val currentMapSource = MutableLiveData<MapSource>()

    init {
        val serialized = storage.read(OSM_TILE_SOURCE, String::class)
        if (serialized != null) {
            currentMapSource.value = config.config.availableSources.first { it.id == serialized }
        } else {
            currentMapSource.value = config.config.defaultMapSourceData.value
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
        storage.save(OSM_TILE_SOURCE, source.id)
    }

    fun currentMapSource(): LiveData<MapSource> = currentMapSource
}
