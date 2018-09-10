package com.devindi.wallpaper.misc

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.os.Bundle
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.archlifecycle.ControllerLifecycleRegistryOwner

abstract class FixedLifecycleController : Controller, LifecycleRegistryOwner {

    constructor(): super() {}
    constructor(args: Bundle): super(args) {}

    private val lifecycleRegistryOwner = ControllerLifecycleRegistryOwner(this)

    override fun getLifecycle(): LifecycleRegistry {
        return lifecycleRegistryOwner.lifecycle
    }
}
