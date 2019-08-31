package com.devindi.wallpaper.misc

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Observer

class NonNullMediatorLiveData<T> : MediatorLiveData<T>() {

    fun observe(owner: LifecycleOwner, observer: (t: T) -> Unit) {
        this.observe(owner, Observer {
            it?.let(observer)
        })
    }
}


fun <T> LiveData<T>.nonNull(): NonNullMediatorLiveData<T> {
    val mediator: NonNullMediatorLiveData<T> = NonNullMediatorLiveData()
    mediator.addSource(this) { it?.let { mediator.value = it } }
    return mediator
}

