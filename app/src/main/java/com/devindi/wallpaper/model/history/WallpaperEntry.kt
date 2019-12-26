package com.devindi.wallpaper.model.history

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity
data class WallpaperEntry(
    val mapSourceId: String,
    val centerLat: Double,
    val centerLon: Double,
    val zoom: Double,
    val width: Int,
    val height: Int,
    val createdAt: Calendar
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
