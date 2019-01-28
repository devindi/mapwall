package com.devindi.wallpaper.model.history

import android.arch.lifecycle.LiveData
import com.devindi.wallpaper.model.storage.wallpaper.WallpaperDao

class HistoryManager(private val wallpaperDao: WallpaperDao) {

    fun addEntry(entry: WallpaperEntry) = wallpaperDao.saveEntry(entry)

    fun getHistory(): LiveData<List<WallpaperEntry>> = wallpaperDao.getHistory()
}
