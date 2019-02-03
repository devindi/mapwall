package com.devindi.wallpaper.model.storage

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.devindi.wallpaper.model.history.WallpaperEntry
import com.devindi.wallpaper.model.storage.wallpaper.WallpaperDao

@Database(entities = [WallpaperEntry::class], version = 1)
@TypeConverters(Converters::class)
abstract class MapwallDatabase : RoomDatabase() {

    abstract fun wallpaperDao(): WallpaperDao
}
