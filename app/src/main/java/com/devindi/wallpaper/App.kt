package com.devindi.wallpaper

import android.app.Application
import android.preference.PreferenceManager
import com.devindi.wallpaper.history.HistoryViewModel
import com.devindi.wallpaper.home.HomeViewModel
import com.devindi.wallpaper.misc.FabricReportManager
import com.devindi.wallpaper.misc.ReportManager
import com.devindi.wallpaper.misc.createPermissionManager
import com.devindi.wallpaper.model.AndroidInfo
import com.devindi.wallpaper.model.DeviceInfo
import com.devindi.wallpaper.model.DisplayInfo
import com.devindi.wallpaper.model.SettingsRepo
import com.devindi.wallpaper.model.config.ConfigManager
import com.devindi.wallpaper.model.map.TileRequestHandler
import com.devindi.wallpaper.model.map.mapModule
import com.devindi.wallpaper.model.search.searchModule
import com.devindi.wallpaper.model.storage.KeyValueStorage
import com.devindi.wallpaper.model.storage.MapCacheStrategy
import com.devindi.wallpaper.model.storage.SharedPreferencesStorage
import com.devindi.wallpaper.model.storage.dbModule
import com.devindi.wallpaper.settings.SettingsViewModel
import com.devindi.wallpaper.settings.model.HeightField
import com.devindi.wallpaper.settings.model.SizeSettingsFactory
import com.devindi.wallpaper.settings.model.WidthField
import com.devindi.wallpaper.settings.size.edit.EditSizeViewModel
import com.devindi.wallpaper.source.MapSourceViewModel
import com.devindi.wallpaper.splash.SplashViewModel
import com.google.firebase.FirebaseApp
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
        single { DeviceInfo(androidApplication()) } bind AndroidInfo::class bind DisplayInfo::class
        single { MapCacheStrategy(androidApplication(), get()) }
        single { ConfigManager() }
        single { Configuration.getInstance() }
        factory { SizeSettingsFactory(PreferenceManager.getDefaultSharedPreferences(get()), get()) }
        viewModel { SplashViewModel(get(), get(), get()) }
        viewModel { HomeViewModel(get(), get(), get(), get(), get(), WidthField(PreferenceManager.getDefaultSharedPreferences(get()), get()), HeightField(PreferenceManager.getDefaultSharedPreferences(get()), get())) }
        viewModel { MapSourceViewModel(get(), get()) }
        viewModel { EditSizeViewModel(get()) }
        viewModel { SettingsViewModel(PreferenceManager.getDefaultSharedPreferences(get()), get()) }
        viewModel { HistoryViewModel(get()) }
    }

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

        val config = Configuration.getInstance()
        config.isDebugMapTileDownloader = false
        config.isDebugMapView = false
        config.isDebugMode = false
        config.isDebugTileProviders = false
        config.userAgentValue = BuildConfig.APPLICATION_ID

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

        startKoin(
            this,
            listOf(applicationModule, searchModule, mapModule, dbModule),
            logger = koinLogger
        )
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
