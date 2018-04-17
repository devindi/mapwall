package com.devindi.wallpaper.source

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import com.devindi.wallpaper.misc.Config
import com.devindi.wallpaper.misc.ConfigManager
import com.devindi.wallpaper.misc.SettingsRepo
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase

class MapSourceViewModel(private val settings: SettingsRepo, private val configManager: ConfigManager): ViewModel() {

    val currentMapSource = MediatorLiveData<OnlineTileSourceBase>()
    val mapSourceList = configManager.config.availableSources

    init {
        currentMapSource.addSource(settings.currentMapSource()) { t -> t?.let { currentMapSource.value = it } }
        currentMapSource.addSource(configManager.config.defaultMapSourceData, { currentMapSource.value = it} )
    }

    fun setCurrentSource(source: OnlineTileSourceBase) {
        settings.setCurrentMapSource(source)
    }
}