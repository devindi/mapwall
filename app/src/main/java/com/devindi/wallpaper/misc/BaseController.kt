package com.devindi.wallpaper.misc

import android.arch.lifecycle.ViewModelStore
import android.arch.lifecycle.ViewModelStoreOwner
import android.os.Bundle
import com.bluelinelabs.conductor.archlifecycle.LifecycleController

/**
 * Inspired by https://github.com/miquelbeltran/conductor-viewmodel
 */
abstract class BaseController(args: Bundle? = null) :
    LifecycleController(args), ViewModelStoreOwner {

    private val viewModelStore = ViewModelStore()

    override fun getViewModelStore(): ViewModelStore {
        return viewModelStore
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModelStore.clear()
    }
}
