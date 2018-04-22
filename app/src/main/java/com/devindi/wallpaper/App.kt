package com.devindi.wallpaper

import android.app.Application
import android.app.WallpaperManager
import android.os.Bundle
import android.preference.PreferenceManager
import com.devindi.wallpaper.home.HomeViewModel
import com.devindi.wallpaper.home.createWallpaperHandler
import com.devindi.wallpaper.misc.*
import com.devindi.wallpaper.model.SettingsRepo
import com.devindi.wallpaper.model.config.ConfigManager
import com.devindi.wallpaper.model.map.*
import com.devindi.wallpaper.search.GoogleApiClientLifecycleObserver
import com.devindi.wallpaper.search.SearchManager
import com.devindi.wallpaper.search.SearchViewModel
import com.devindi.wallpaper.source.MapSourceViewModel
import com.devindi.wallpaper.splash.SplashViewModel
import com.devindi.wallpaper.model.storage.KeyValueStorage
import com.devindi.wallpaper.model.storage.SharedPreferencesStorage
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import com.squareup.picasso.Picasso
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.get
import org.koin.android.ext.android.startKoin
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext
import org.koin.log.Logger
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.cachemanager.CacheManager
import org.osmdroid.tileprovider.modules.IFilesystemCache
import org.osmdroid.tileprovider.modules.SqlTileWriter
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import timber.log.Timber

const val PARAM_TILE_SOURCE = "tile"

class App : Application() {

    private val applicationModule : Module = applicationContext {
        bean { SharedPreferencesStorage(PreferenceManager.getDefaultSharedPreferences(get())) as KeyValueStorage }
        viewModel { SplashViewModel(get()) }
        bean { SettingsRepo(get(), get()) }
        bean { createPermissionManager() }
        bean { createWallpaperHandler(WallpaperManager.getInstance(androidApplication())) }
        bean { Configuration.getInstance() }
        bean { SqlTileWriter() as IFilesystemCache }
        viewModel { HomeViewModel(get(), get(), get()) }
        factory { params ->
            CacheManager(params.get<OnlineTileSourceBase>(PARAM_TILE_SOURCE),
                    get(),
                    params.get<OnlineTileSourceBase>(PARAM_TILE_SOURCE).minimumZoomLevel,
                    params.get<OnlineTileSourceBase>(PARAM_TILE_SOURCE).maximumZoomLevel)
        }
        factory { params ->
            WallpaperFactory(get { params.values }, params[PARAM_TILE_SOURCE], get(), get())
        }
        bean { FabricReportManager() as ReportManager }
        viewModel { SearchViewModel(get()) }
        bean { SearchManager(get()) }
        bean {
            GoogleApiClient.Builder(androidApplication())
                    .addApi(Places.GEO_DATA_API)
                    .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                        override fun onConnected(p0: Bundle?) {
                            Timber.d("Connected $p0")
                        }

                        override fun onConnectionSuspended(p0: Int) {
                            Timber.d("Suspended $p0")
                        }
                    })
                    .addOnConnectionFailedListener { Timber.d("Failed $it") }
                    .build()
        }
        bean { GoogleApiClientLifecycleObserver(get()) }
        viewModel { MapSourceViewModel(get(), get()) }
        bean { ConfigManager() }
        bean { SyncMapTileProvider(get(), get()) }
        bean { MapAreaManager(get(), get()) }
        bean { CacheManagerFactory(get()) }
        bean { TileSourceFactory(get()) }
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
//                Timber.d(msg)
            }

            override fun err(msg: String) {
//                Timber.e(msg)
            }

            override fun log(msg: String) {
//                Timber.v(msg)
            }
        }

        startKoin(this, listOf(applicationModule), logger = koinLogger)
        val reportManager: ReportManager = get()
        reportManager.init(this)

        val manager: MapAreaManager = get()

        val picasso = Picasso.Builder(this).loggingEnabled(true).addRequestHandler(TileRequestHandler(manager)).listener { _, uri, exception -> Timber.e(exception, "Failed to load $uri") }.build()
        Picasso.setSingletonInstance(picasso)
    }
}




