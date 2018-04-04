package com.devindi.wallpaper

import android.app.Activity
import android.os.Bundle
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.devindi.wallpaper.home.HomeController
import org.osmdroid.config.Configuration

class MainActivity: Activity() {

    private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.root_screen)

        val config = Configuration.getInstance()
        config.isDebugMapTileDownloader = true
        config.isDebugMapView = true
        config.isDebugMode = true
        config.isDebugTileProviders = true

        router = Conductor.attachRouter(this, findViewById(R.id.container), savedInstanceState)
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(HomeController()))
        }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }
}