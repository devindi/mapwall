package com.devindi.wallpaper.settings.model

import android.arch.lifecycle.MutableLiveData
import android.support.annotation.CallSuper
import com.devindi.wallpaper.misc.NonNullMediatorLiveData
import com.devindi.wallpaper.misc.nonNull

abstract class SettingsField<T> {

    private val liveData = MutableLiveData<T>()

    abstract val key: String

    abstract val titleId: Int

    abstract fun get(): T

    abstract fun get(fallback: T): T

    @CallSuper
    open fun set(value: T) {
        liveData.value = value
    }

    fun observe(): NonNullMediatorLiveData<T> {
        liveData.value = this.get()
        return liveData.nonNull()
    }
}
