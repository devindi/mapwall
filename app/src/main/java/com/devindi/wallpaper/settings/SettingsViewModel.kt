package com.devindi.wallpaper.settings

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.SharedPreferences
import com.devindi.wallpaper.misc.NonNullMediatorLiveData
import com.devindi.wallpaper.misc.nonNull
import com.devindi.wallpaper.settings.model.DIMENSION_HEIGHT
import com.devindi.wallpaper.settings.model.DIMENSION_WIDTH
import com.devindi.wallpaper.settings.model.SettingsField
import com.devindi.wallpaper.settings.model.SizeSettingsFactory
import com.devindi.wallpaper.settings.model.StorageTargetField
import com.devindi.wallpaper.settings.model.WallpaperTargetField
import timber.log.Timber

class SettingsViewModel(
    storage: SharedPreferences,
    sizeSettingsFactory: SizeSettingsFactory
) : ViewModel() {

    private val mediator = MediatorLiveData<List<SettingsField<*>>>()

    init {
        val fields = listOf(
            sizeSettingsFactory.createField(DIMENSION_HEIGHT),
            sizeSettingsFactory.createField(DIMENSION_WIDTH),
            WallpaperTargetField(storage),
            StorageTargetField(storage))

        mediator.value = fields

        fields.forEach { a ->
            mediator.addSource(a.observe()) { b ->
                Timber.e("Somth changed $a $b")
                mediator.value = fields
            }
        }
    }

    fun settings(): NonNullMediatorLiveData<List<SettingsField<*>>> = mediator.nonNull()
}
