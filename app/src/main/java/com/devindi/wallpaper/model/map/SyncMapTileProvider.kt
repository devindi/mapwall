package com.devindi.wallpaper.model.map

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import org.osmdroid.tileprovider.cachemanager.CacheManager
import org.osmdroid.tileprovider.modules.IFilesystemCache
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase

/**
 * Synchronous analog of [org.osmdroid.tileprovider.MapTileProviderBase]
 */
class SyncMapTileProvider(
    private val factory: CacheManagerFactory,
    private val cache: IFilesystemCache
) {

    private val cacheOfCaches: MutableMap<OnlineTileSourceBase, CacheManager> = mutableMapOf()

    /**
     * Load tile from online tile source if it's absent at cache and put it into the cache. If tile
     * loading failed method will throw exception
     * @return tile from cache
     */
    fun getTile(tileSource: OnlineTileSourceBase, tileIndex: Long): Bitmap {
        val cacheManager = getCacheManager(tileSource)
        if (!cacheManager.checkTile(tileIndex)) {
            cacheManager.downloadingAction.run { preCheck() && tileAction(tileIndex) }
        }
        val tileDrawable = cache.loadTile(tileSource, tileIndex) as BitmapDrawable
        return tileDrawable.bitmap
    }

    private fun getCacheManager(tileSource: OnlineTileSourceBase) =
        cacheOfCaches.getOrPut(tileSource) { factory.create(tileSource) }
}

class CacheManagerFactory(private val cache: IFilesystemCache) {
    fun create(tileSource: OnlineTileSourceBase) =
        CacheManager(tileSource, cache, tileSource.minimumZoomLevel, tileSource.maximumZoomLevel)
}
