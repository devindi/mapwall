package com.devindi.wallpaper

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.devindi.wallpaper.home.HomeController
import com.devindi.wallpaper.search.GoogleApiClientLifecycleObserver
import com.devindi.wallpaper.splash.SplashController
import com.devindi.wallpaper.splash.SplashViewModel
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.inject

class MainActivity: AppCompatActivity() {

    private lateinit var router: Router
    private val splashViewModel: SplashViewModel by viewModel()
    private val googleApiClientObserver: GoogleApiClientLifecycleObserver by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.root_screen)

        lifecycle.addObserver(googleApiClientObserver)

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