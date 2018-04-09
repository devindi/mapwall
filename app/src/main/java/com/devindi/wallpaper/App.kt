package com.devindi.wallpaper

import android.app.Application
import android.preference.PreferenceManager
import com.devindi.wallpaper.misc.SettingsRepo
import com.devindi.wallpaper.misc.createPermissionManager
import com.devindi.wallpaper.splash.SplashViewModel
import com.devindi.wallpaper.storage.KeyValueStorage
import com.devindi.wallpaper.storage.SharedPreferencesStorage
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext
import org.osmdroid.config.Configuration

class App : Application() {

    private val applicationModule : Module = applicationContext {
        bean { SharedPreferencesStorage(PreferenceManager.getDefaultSharedPreferences(get())) as KeyValueStorage }
        bean { SplashViewModel(get()) }
        bean { SettingsRepo(get()) }
        bean { createPermissionManager() }
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





