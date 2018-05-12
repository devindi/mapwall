package com.devindi.wallpaper.splash

import android.arch.lifecycle.ViewModel
import com.devindi.wallpaper.misc.SingleLiveEvent
import com.devindi.wallpaper.model.SettingsRepo
import com.devindi.wallpaper.model.storage.MapCacheStrategy

class SplashViewModel(private val settings: SettingsRepo, private val mapCacheStrategy: MapCacheStrategy) : ViewModel() {

    val shouldAskFroCacheLocation = SingleLiveEvent<Boolean>()
    val appInitialized = SingleLiveEvent<Boolean>()

    init {
        val defaultPath = mapCacheStrategy.defaultCachePath()
        if (defaultPath == null) {
            shouldAskFroCacheLocation.sendEvent(true)
        } else {
            shouldAskFroCacheLocation.sendEvent(false)
            saveMapCachePath(defaultPath)
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
        appInitialized.sendEvent(true)
    }
}
