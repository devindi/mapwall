package com.devindi.wallpaper.misc

import com.devindi.wallpaper.storage.KeyValueStorage

private const val OSM_CACHE_PATH = "osm"

class SettingsRepo(private val storage: KeyValueStorage) {

    fun getMapCachePath(): String? {
        return storage.read(OSM_CACHE_PATH, String::class)
    }

    fun setMapCachePath(path: String) {
        storage.save(OSM_CACHE_PATH, path)
    }
}