package com.devindi.wallpaper.model.map

import android.app.WallpaperManager
import com.devindi.wallpaper.home.createWallpaperHandler
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.Module
import org.koin.dsl.module.module
import org.osmdroid.tileprovider.cachemanager.CacheManager
import org.osmdroid.tileprovider.modules.IFilesystemCache
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase

val mapModule: Module = module {
    single { SqlTileCache(get()) as IFilesystemCache }
    single { createWallpaperHandler(WallpaperManager.getInstance(androidApplication())) }
    factory { (source: OnlineTileSourceBase) ->
        CacheManager(source, get(), source.minimumZoomLevel, source.maximumZoomLevel)
    }
    single { SyncMapTileProvider(get(), get()) }
    single { MapAreaManager(get(), get()) }
    single { CacheManagerFactory(get()) }
    single { TileSourceFactory(get()) }
}
