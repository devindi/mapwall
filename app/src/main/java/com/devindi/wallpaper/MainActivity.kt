package com.devindi.wallpaper

import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.devindi.wallpaper.home.HomeController
import com.devindi.wallpaper.misc.InsetsProvider
import com.devindi.wallpaper.splash.SplashController
import org.koin.android.scope.ext.android.bindScope
import org.koin.android.scope.ext.android.getOrCreateScope

class MainActivity : AppCompatActivity(), InsetsProvider {

    lateinit var router: Router
    private var topInset = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.root_screen)
        val rootView = findViewById<ViewGroup>(R.id.container)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) {
            _, insets -> topInset = insets.systemWindowInsetTop
            return@setOnApplyWindowInsetsListener insets.consumeSystemWindowInsets()
        }
        router = Conductor.attachRouter(this, rootView, savedInstanceState)
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(SplashController()))
        }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }

    override fun topInset(): Int {
        return topInset
    }

    fun openMainScreen() {
        bindScope(getOrCreateScope("map_scope"))
        router.setRoot(RouterTransaction.with(HomeController()))
    }
}
