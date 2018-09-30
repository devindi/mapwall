package com.devindi.wallpaper.misc

import android.arch.lifecycle.ViewModelStore
import android.arch.lifecycle.ViewModelStoreOwner
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.bluelinelabs.conductor.archlifecycle.LifecycleController

abstract class BaseController(args: Bundle? = null) :
    LifecycleController(args),
    ViewModelStoreOwner {

    override fun getViewModelStore(): ViewModelStore {
        return (activity as FragmentActivity).viewModelStore
    }
}