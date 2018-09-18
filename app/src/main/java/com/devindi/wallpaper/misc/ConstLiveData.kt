package com.devindi.wallpaper.misc

import android.arch.lifecycle.LiveData

/**
 * Write-once live data implementation
 */
class ConstLiveData<T>(t: T) : LiveData<T>() {
    init {
        value = t
    }
}
