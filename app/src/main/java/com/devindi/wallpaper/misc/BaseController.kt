package com.devindi.wallpaper.misc

import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import android.os.Bundle
import com.bluelinelabs.conductor.archlifecycle.LifecycleController

/**
 * Inspired by https://github.com/miquelbeltran/conductor-viewmodel
 */
abstract class BaseController(args: Bundle? = null) :
    LifecycleController(args), ViewModelStoreOwner {

    private val viewModelStore = ViewModelStore()

    protected fun getTopInset() = (activity as InsetsProvider).topInset()

    override fun getViewModelStore(): ViewModelStore {
        return viewModelStore
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModelStore.clear()
    }
}
