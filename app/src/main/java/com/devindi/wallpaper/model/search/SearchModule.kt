package com.devindi.wallpaper.model.search

import com.devindi.wallpaper.model.places.GoogleApiErrorHandler
import com.devindi.wallpaper.search.GoogleApiClientLifecycleObserver
import com.devindi.wallpaper.search.SearchManager
import com.devindi.wallpaper.search.SearchViewModel
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

val searchModule: Module = module {
    viewModel { SearchViewModel(get(), get()) }
    single { GoogleApiErrorHandler(get()) } bind GoogleApiClient.OnConnectionFailedListener::class
    single { SearchManager(Places.GeoDataApi, get(), get()) }
    single { GoogleApiClientLifecycleObserver(get()) }
    single {
        GoogleApiClient.Builder(androidApplication())
            .addApi(Places.GEO_DATA_API)
            .addOnConnectionFailedListener(get())
            .build()
    }
}
