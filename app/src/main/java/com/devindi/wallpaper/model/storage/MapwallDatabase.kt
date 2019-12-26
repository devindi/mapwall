package com.devindi.wallpaper.model.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.devindi.wallpaper.model.history.WallpaperEntry
import com.devindi.wallpaper.model.storage.wallpaper.WallpaperDao

@Database(entities = [WallpaperEntry::class], version = 1)
@TypeConverters(Converters::class)
abstract class MapwallDatabase : RoomDatabase() {

    abstract fun wallpaperDao(): WallpaperDao
}
