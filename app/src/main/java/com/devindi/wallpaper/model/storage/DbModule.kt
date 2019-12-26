package com.devindi.wallpaper.model.storage

import androidx.room.Room
import com.devindi.wallpaper.model.history.HistoryManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

val dbModule = module {
    single { Room
        .databaseBuilder(androidContext(), MapwallDatabase::class.java, "mapwall.db")
        .allowMainThreadQueries()
        .build()
    }
    single { get<MapwallDatabase>().wallpaperDao() }
    factory { HistoryManager(get()) }
}
