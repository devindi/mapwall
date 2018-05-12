package com.devindi.wallpaper.model.map

import android.app.WallpaperManager
import com.devindi.wallpaper.PARAM_TILE_SOURCE
import com.devindi.wallpaper.home.createWallpaperHandler
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.cachemanager.CacheManager
import org.osmdroid.tileprovider.modules.IFilesystemCache
import org.osmdroid.tileprovider.modules.SqlTileWriter
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase

val mapModule: Module = applicationContext {
    bean { SqlTileWriter() as IFilesystemCache }
    bean { Configuration.getInstance() }
    bean { createWallpaperHandler(WallpaperManager.getInstance(androidApplication())) }
    factory { params ->
        CacheManager(params.get<OnlineTileSourceBase>(PARAM_TILE_SOURCE),
                get(),
                params.get<OnlineTileSourceBase>(PARAM_TILE_SOURCE).minimumZoomLevel,
                params.get<OnlineTileSourceBase>(PARAM_TILE_SOURCE).maximumZoomLevel)
    }
    bean { SyncMapTileProvider(get(), get()) }
    bean { MapAreaManager(get(), get()) }
    bean { CacheManagerFactory(get()) }
    bean { TileSourceFactory(get()) }
}
