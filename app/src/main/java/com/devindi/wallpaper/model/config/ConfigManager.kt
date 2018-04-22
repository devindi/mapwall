package com.devindi.wallpaper.model.config

import timber.log.Timber

class ConfigManager {

    val config: Config by lazy {
        Timber.d("Loading config")
        Config()
    }
}
