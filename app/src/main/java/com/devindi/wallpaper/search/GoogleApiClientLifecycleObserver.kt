package com.devindi.wallpaper.search

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.google.android.gms.common.api.GoogleApiClient

class GoogleApiClientLifecycleObserver(private val client: GoogleApiClient): LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        client.connect()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        client.disconnect()
    }
}
