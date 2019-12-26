package com.devindi.wallpaper.model.storage.wallpaper

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.devindi.wallpaper.model.history.WallpaperEntry

@Dao
interface WallpaperDao {

    @Query("SELECT * FROM wallpaperentry ORDER BY createdAt DESC")
    fun getHistory(): LiveData<List<WallpaperEntry>>

    @Insert
    fun saveEntry(entry: WallpaperEntry)
}
