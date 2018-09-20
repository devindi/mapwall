package com.devindi.wallpaper

import android.app.Application
import android.preference.PreferenceManager
import com.devindi.wallpaper.home.HomeViewModel
import com.devindi.wallpaper.misc.DependencyStrategy
import com.devindi.wallpaper.misc.FabricReportManager
import com.devindi.wallpaper.misc.ReportManager
import com.devindi.wallpaper.misc.createPermissionManager
import com.devindi.wallpaper.model.AndroidInfo
import com.devindi.wallpaper.model.DeviceInfo
import com.devindi.wallpaper.model.SettingsRepo
import com.devindi.wallpaper.model.config.ConfigManager
import com.devindi.wallpaper.model.map.TileRequestHandler
import com.devindi.wallpaper.model.search.searchModule
import com.devindi.wallpaper.model.storage.KeyValueStorage
import com.devindi.wallpaper.model.storage.MapCacheStrategy
import com.devindi.wallpaper.model.storage.SharedPreferencesStorage
import com.devindi.wallpaper.source.MapSourceViewModel
import com.devindi.wallpaper.splash.SplashViewModel
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.get
import org.koin.android.ext.android.startKoin
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.module
import org.koin.log.Logger
import org.osmdroid.config.Configuration
import timber.log.Timber

class App : Application() {

    private val applicationModule: Module = module {
        single {
            SharedPreferencesStorage(PreferenceManager.getDefaultSharedPreferences(get()))
                as KeyValueStorage
        }
        single { SettingsRepo(get(), get()) }
        single { createPermissionManager() }
        single { FabricReportManager() as ReportManager }
        single { DeviceInfo(androidApplication()) as AndroidInfo }
        single { MapCacheStrategy(androidApplication(), get()) }
        single { ConfigManager() }
        single { DependencyStrategy(get(), get()) }
        single { Configuration.getInstance() }
        viewModel { SplashViewModel(get(), get()) }
        viewModel { HomeViewModel(get(), get(), get(), get()) }
        viewModel { MapSourceViewModel(get(), get()) }
    }

    override fun onCreate() {
        super.onCreate()

        val config = Configuration.getInstance()
        config.isDebugMapTileDownloader = false
        config.isDebugMapView = false
        config.isDebugMode = false
        config.isDebugTileProviders = false

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        val koinLogger = object : Logger {
            override fun debug(msg: String) {
                Timber.d(msg)
            }

            override fun err(msg: String) {
                Timber.e(msg)
            }

            override fun info(msg: String) {
                Timber.i(msg)
            }
        }

        startKoin(this, listOf(applicationModule, searchModule), logger = koinLogger)
        val reportManager: ReportManager = get()
        reportManager.init(this)

        val picasso = Picasso.Builder(get())
            .loggingEnabled(true)
            .addRequestHandler(TileRequestHandler())
            .listener { _, uri, exception -> Timber.e(exception, "Failed to load $uri") }
            .build()
        Picasso.setSingletonInstance(picasso)
    }
}
