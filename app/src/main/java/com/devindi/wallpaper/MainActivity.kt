package com.devindi.wallpaper

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.devindi.wallpaper.splash.SplashController

class MainActivity: AppCompatActivity() {

    private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.root_screen)

        router = Conductor.attachRouter(this, findViewById(R.id.container), savedInstanceState)
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(SplashController()))
        }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }
}
