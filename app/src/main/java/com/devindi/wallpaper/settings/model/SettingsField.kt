package com.devindi.wallpaper.settings.model

import androidx.lifecycle.MutableLiveData
import com.devindi.wallpaper.misc.NonNullMediatorLiveData
import com.devindi.wallpaper.misc.nonNull

abstract class SettingsField<T> {

    protected val liveData = MutableLiveData<T>()

    abstract val key: String

    abstract val titleId: Int

    abstract fun get(): T

    abstract fun get(fallback: T): T

    abstract fun set(value: T)

    fun observe(): NonNullMediatorLiveData<T> {
        liveData.value = this.get()
        return liveData.nonNull()
    }
}
