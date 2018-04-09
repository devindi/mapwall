package com.devindi.wallpaper

import android.app.Activity
import android.os.Bundle
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.devindi.wallpaper.home.HomeController
import com.devindi.wallpaper.splash.SplashController
import com.devindi.wallpaper.splash.SplashViewModel
import org.koin.android.ext.android.inject

class MainActivity: Activity() {

    private lateinit var router: Router
    private val splashViewModel: SplashViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.root_screen)

        val target: Controller = if(splashViewModel.haveMapCachePath()) HomeController() else SplashController()

        router = Conductor.attachRouter(this, findViewById(R.id.container), savedInstanceState)
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(target))
        }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }
}