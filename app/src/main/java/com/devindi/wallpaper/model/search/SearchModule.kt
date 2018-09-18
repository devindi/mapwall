package com.devindi.wallpaper.model.search

import com.devindi.wallpaper.model.places.GoogleApiErrorHandler
import com.devindi.wallpaper.search.GoogleApiClientLifecycleObserver
import com.devindi.wallpaper.search.SearchManager
import com.devindi.wallpaper.search.SearchViewModel
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext

val searchModule: Module = applicationContext {
    viewModel { SearchViewModel(get(), get()) }
    bean { GoogleApiErrorHandler(get()) } bind GoogleApiClient.OnConnectionFailedListener::class
    bean { SearchManager(get(), get()) }
    bean { GoogleApiClientLifecycleObserver(get()) }
    bean {
        GoogleApiClient.Builder(androidApplication())
            .addApi(Places.GEO_DATA_API)
            .addOnConnectionFailedListener(get())
            .build()
    }
}
