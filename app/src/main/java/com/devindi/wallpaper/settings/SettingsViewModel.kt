package com.devindi.wallpaper.settings

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.content.SharedPreferences
import com.devindi.wallpaper.misc.NonNullMediatorLiveData
import com.devindi.wallpaper.misc.nonNull
import com.devindi.wallpaper.settings.model.DIMENSION_HEIGHT
import com.devindi.wallpaper.settings.model.DIMENSION_WIDTH
import com.devindi.wallpaper.settings.model.SettingsField
import com.devindi.wallpaper.settings.model.SettingsManager
import com.devindi.wallpaper.settings.model.StorageTargetField
import com.devindi.wallpaper.settings.model.WallpaperTargetField

class SettingsViewModel(private val settingsManager: SettingsManager, private val storage: SharedPreferences) : ViewModel() {

    private val items = MutableLiveData<List<SettingsField<*>>>()
    private val settingsObserver = Observer<String> {
        notifySettingsChange()
    }

    init {
        notifySettingsChange()
        settingsManager.fieldChange().observeForever(settingsObserver)
    }

    fun settings(): NonNullMediatorLiveData<List<SettingsField<*>>> = items.nonNull()

    override fun onCleared() {
        settingsManager.fieldChange().removeObserver(settingsObserver)
    }

    private fun notifySettingsChange() {
        items.value = listOf(
            settingsManager.getIntField(DIMENSION_HEIGHT),
            settingsManager.getIntField(DIMENSION_WIDTH),
            WallpaperTargetField(storage),
            StorageTargetField(storage))
    }
}
