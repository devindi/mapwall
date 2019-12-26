package com.devindi.wallpaper.splash

import androidx.lifecycle.ViewModel
import com.devindi.wallpaper.misc.SingleLiveEvent
import com.devindi.wallpaper.model.SettingsRepo
import com.devindi.wallpaper.model.storage.MapCacheStrategy
import org.osmdroid.config.IConfigurationProvider
import java.io.File

class SplashViewModel(
    private val settings: SettingsRepo,
    private val mapCacheStrategy: MapCacheStrategy,
    private val osmConfig: IConfigurationProvider
) : ViewModel() {

    val shouldAskFroCacheLocation = SingleLiveEvent<Boolean>()
    val appInitialized = SingleLiveEvent<Boolean>()

    init {
        val userPath = settings.getMapCachePath()
        if (userPath != null) {
            osmConfig.osmdroidBasePath = File(userPath)
            appInitialized.sendEvent(true)
        } else {
            val defaultPath = mapCacheStrategy.defaultCachePath()
            if (defaultPath == null) {
                shouldAskFroCacheLocation.sendEvent(true)
            } else {
                saveMapCachePath(defaultPath)
            }
        }
    }

    fun useInternalStorage() {
        saveMapCachePath(mapCacheStrategy.createInternalPath())
    }

    fun useExternalStorage() {
        saveMapCachePath(mapCacheStrategy.createExternalPath())
    }

    private fun saveMapCachePath(path: String) {
        settings.setMapCachePath(path)
        osmConfig.osmdroidBasePath = File(path)
        appInitialized.sendEvent(true)
    }
}
