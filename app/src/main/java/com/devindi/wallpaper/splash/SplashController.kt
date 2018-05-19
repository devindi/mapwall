package com.devindi.wallpaper.splash

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.archlifecycle.LifecycleController
import com.devindi.wallpaper.home.HomeController
import com.devindi.wallpaper.misc.*
import com.devindi.wallpaper.model.analytics.ScreenEvent
import com.devindi.wallpaper.model.map.mapModule
import org.koin.standalone.StandAloneContext

class SplashController: LifecycleController() {

    private val permissionManager: PermissionManager by inject()
    private val viewModel: SplashViewModel by viewModel()
    private val dependencyStrategy: DependencyStrategy by inject()
    private val reportManager: ReportManager by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return View(container.context)
    }

    override fun onActivityResumed(activity: Activity) {
        super.onActivityResumed(activity)
        viewModel.shouldAskFroCacheLocation.observe(this, Observer {
            if (it != null && it) {
                askForCacheLocation(activity)
            }
        })
        viewModel.appInitialized.observe(this, Observer {
            dependencyStrategy.initMapModule()
            router.setRoot(RouterTransaction.with(HomeController()))
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val granted = permissions
                .filterIndexed { index, _ -> grantResults[index] == PackageManager.PERMISSION_GRANTED }
                .any { it == Manifest.permission.WRITE_EXTERNAL_STORAGE }
        if (granted) {
            viewModel.useExternalStorage()
        }
        activity?.let {
            askForCacheLocation(it)
        }
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        reportManager.reportEvent(ScreenEvent("splash"))
    }

    private fun askForCacheLocation(activity: Activity) {
        AlertDialog.Builder(activity)
                .setMessage("Select map cache location. Disk usage is based on map usage and be up to 1GB. External storage is recommended")
                .setPositiveButton("Internal") { _, _ ->
                    viewModel.useInternalStorage()
                }
                .setNegativeButton("External") { _, _ ->
                    val requiredPermissions = permissionManager.checkPermissions(listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), activity)
                    if (requiredPermissions.isEmpty()) {
                        viewModel.useExternalStorage()
                    } else {
                        permissionManager.requestPermissions(requiredPermissions, activity)
                    }
                }
                .show()
    }
}
