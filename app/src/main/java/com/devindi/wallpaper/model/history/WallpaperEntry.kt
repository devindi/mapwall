package com.devindi.wallpaper.model.history

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.Calendar

@Entity
data class WallpaperEntry(
    val mapSourceId: String,
    val centerLat: Double,
    val centerLon: Double,
    val width: Int,
    val height: Int,
    val createdAt: Calendar
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
