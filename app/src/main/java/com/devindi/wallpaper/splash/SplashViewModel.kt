package com.devindi.wallpaper.splash

import android.arch.lifecycle.ViewModel
import com.devindi.wallpaper.model.SettingsRepo

class SplashViewModel(private val settings: SettingsRepo) : ViewModel() {

    fun haveMapCachePath(): Boolean {
        return !settings.getMapCachePath().isNullOrEmpty()
    }

    fun saveMapCachePath(path: String) {
        settings.setMapCachePath(path)
    }
}
