package com.devindi.wallpaper.model.map

import com.devindi.wallpaper.home.ExportWallpaperHandler
import com.devindi.wallpaper.home.WallpaperHandler
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import org.osmdroid.tileprovider.cachemanager.CacheManager
import org.osmdroid.tileprovider.modules.IFilesystemCache
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase

val mapModule = module {
    scope("map_scope") { SqlTileCache(get()) as IFilesystemCache }
    scope("map_scope") {
        //        createWallpaperHandler(WallpaperManager.getInstance(androidApplication()))
        ExportWallpaperHandler(androidContext().cacheDir) as WallpaperHandler
    }
    factory { (source: OnlineTileSourceBase) ->
        CacheManager(source, get(), source.minimumZoomLevel, source.maximumZoomLevel)
    }
    scope("map_scope") { SyncMapTileProvider(get(), get()) }
    scope("map_scope") { CacheManagerFactory(get()) }
    scope("map_scope") { TileSourceFactory(get()) }
    scope("map_scope") { MapImageGenerator(get(), get(), get()) }
    scope("map_scope") { TileUtils() }
}
