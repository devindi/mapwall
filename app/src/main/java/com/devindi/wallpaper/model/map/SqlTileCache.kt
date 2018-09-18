package com.devindi.wallpaper.model.map

import com.devindi.wallpaper.misc.ReportManager
import org.osmdroid.tileprovider.modules.SqlTileWriter

class SqlTileCache(reportManager: ReportManager) : SqlTileWriter() {

    init {
        if (db == null) {
            reportManager.reportError(IllegalStateException("Failed to open db"))
        }
    }
}