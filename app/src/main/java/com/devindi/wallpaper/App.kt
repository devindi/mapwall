package com.devindi.wallpaper

import android.app.Application
import android.app.WallpaperManager
import android.preference.PreferenceManager
import com.devindi.wallpaper.home.HomeViewModel
import com.devindi.wallpaper.home.WallpaperFactory
import com.devindi.wallpaper.home.createWallpaperHandler
import com.devindi.wallpaper.misc.SettingsRepo
import com.devindi.wallpaper.misc.createPermissionManager
import com.devindi.wallpaper.splash.SplashViewModel
import com.devindi.wallpaper.storage.KeyValueStorage
import com.devindi.wallpaper.storage.SharedPreferencesStorage
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.startKoin
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.cachemanager.CacheManager
import org.osmdroid.tileprovider.modules.IFilesystemCache
import org.osmdroid.tileprovider.modules.SqlTileWriter
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase

const val PARAM_TILE_SOURCE = "tile"

class App : Application() {

    private val applicationModule : Module = applicationContext {
        bean { SharedPreferencesStorage(PreferenceManager.getDefaultSharedPreferences(get())) as KeyValueStorage }
        viewModel { SplashViewModel(get()) }
        bean { SettingsRepo(get()) }
        bean { createPermissionManager() }
        bean { createWallpaperHandler(WallpaperManager.getInstance(androidApplication())) }
        bean { Configuration.getInstance() }
        bean { SqlTileWriter() as IFilesystemCache }
        viewModel { HomeViewModel(get()) }
        factory { params ->
            CacheManager(params.get<OnlineTileSourceBase>(PARAM_TILE_SOURCE),
                    get(),
                    params.get<OnlineTileSourceBase>(PARAM_TILE_SOURCE).minimumZoomLevel,
                    params.get<OnlineTileSourceBase>(PARAM_TILE_SOURCE).maximumZoomLevel)
        }
        factory { params ->
            WallpaperFactory(get { params.values }, params[PARAM_TILE_SOURCE], get())
        }
    }

    override fun onCreate() {
        super.onCreate()

        val config = Configuration.getInstance()
//        config.isDebugMapTileDownloader = true
//        config.isDebugMapView = true
//        config.isDebugMode = true
        config.isDebugTileProviders = true

        startKoin(this, listOf(applicationModule))
    }
}





