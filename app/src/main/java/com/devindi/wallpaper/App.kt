package com.devindi.wallpaper

import android.app.Application
import android.preference.PreferenceManager
import com.devindi.wallpaper.home.HomeViewModel
import com.devindi.wallpaper.misc.DependencyStrategy
import com.devindi.wallpaper.misc.FabricReportManager
import com.devindi.wallpaper.misc.ReportManager
import com.devindi.wallpaper.misc.createPermissionManager
import com.devindi.wallpaper.model.SettingsRepo
import com.devindi.wallpaper.model.config.ConfigManager
import com.devindi.wallpaper.model.search.searchModule
import com.devindi.wallpaper.model.storage.KeyValueStorage
import com.devindi.wallpaper.model.storage.MapCacheStrategy
import com.devindi.wallpaper.model.storage.SharedPreferencesStorage
import com.devindi.wallpaper.source.MapSourceViewModel
import com.devindi.wallpaper.splash.SplashViewModel
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.get
import org.koin.android.ext.android.startKoin
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext
import org.koin.log.Logger
import org.osmdroid.config.Configuration
import timber.log.Timber

const val PARAM_TILE_SOURCE = "tile"

class App : Application() {

    private val applicationModule : Module = applicationContext {
        bean { SharedPreferencesStorage(PreferenceManager.getDefaultSharedPreferences(get())) as KeyValueStorage }
        bean { SettingsRepo(get(), get()) }
        bean { createPermissionManager() }
        bean { FabricReportManager() as ReportManager }
        bean { MapCacheStrategy(androidApplication()) }
        bean { ConfigManager() }
        bean { DependencyStrategy(get(), get()) }
        bean { Configuration.getInstance() }
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

            override fun log(msg: String) {
                Timber.v(msg)
            }
        }

        startKoin(this, listOf(applicationModule, searchModule), logger = koinLogger)
        val reportManager: ReportManager = get()
        reportManager.init(this)
    }
}





