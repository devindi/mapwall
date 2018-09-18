package com.devindi.wallpaper.source

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import com.devindi.wallpaper.misc.ConstLiveData
import com.devindi.wallpaper.model.SettingsRepo
import com.devindi.wallpaper.model.config.ConfigManager
import com.devindi.wallpaper.model.map.MapSource

class MapSourceViewModel(
    private val settings: SettingsRepo,
    configManager: ConfigManager
) : ViewModel() {

    val currentMapSource = MediatorLiveData<MapSource>()
    val mapSourceList = ConstLiveData(configManager.config.availableSources)

    init {
        currentMapSource.addSource(configManager.config.defaultMapSourceData) {
            currentMapSource.value = it
        }
        currentMapSource.addSource(settings.currentMapSource()) {
            t -> t?.let { currentMapSource.value = it }
        }
    }

    fun setCurrentSource(source: MapSource) {
        settings.setCurrentMapSource(source)
    }
}