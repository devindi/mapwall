package com.devindi.wallpaper.misc

import com.devindi.wallpaper.model.SettingsRepo
import com.devindi.wallpaper.model.map.mapModule
import org.koin.standalone.KoinComponent
import org.koin.standalone.StandAloneContext
import org.osmdroid.config.IConfigurationProvider
import java.io.File

class DependencyStrategy(
    private val settings: SettingsRepo,
    private val osmConfig: IConfigurationProvider
) : KoinComponent {

    /**
     * Loads [mapModule] to koin
     */
    fun initMapModule() {
        osmConfig.osmdroidBasePath = File(settings.getMapCachePath())
        StandAloneContext.loadKoinModules(mapModule)
    }
}
