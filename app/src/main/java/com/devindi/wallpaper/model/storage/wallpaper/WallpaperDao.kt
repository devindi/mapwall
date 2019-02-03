package com.devindi.wallpaper.model.storage.wallpaper

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.devindi.wallpaper.model.history.WallpaperEntry

@Dao
interface WallpaperDao {

    @Query("SELECT * FROM wallpaperentry ORDER BY createdAt DESC")
    fun getHistory(): LiveData<List<WallpaperEntry>>

    @Insert
    fun saveEntry(entry: WallpaperEntry)
}
