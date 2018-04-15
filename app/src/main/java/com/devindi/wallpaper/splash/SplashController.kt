package com.devindi.wallpaper.splash

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.archlifecycle.LifecycleController
import com.devindi.wallpaper.home.HomeController
import com.devindi.wallpaper.misc.PermissionManager
import com.devindi.wallpaper.misc.inject
import com.devindi.wallpaper.misc.viewModel
import java.io.File

class SplashController: LifecycleController() {

    private val permissionManager: PermissionManager by inject()
    private val context: Context by inject()
    private val viewModel: SplashViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return View(container.context)
    }

    override fun onActivityResumed(activity: Activity) {
        super.onActivityResumed(activity)
        val requiredPermissions = permissionManager.checkPermissions(listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), activity)
        if (requiredPermissions.isNotEmpty()) {
            AlertDialog.Builder(activity)
                    .setMessage("Нам нада диск! (1Gb)")
                    .setPositiveButton("Internal") { _, _ ->
                        savePath(createInternalPath())
                    }
                    .setNegativeButton("External") { _, _ ->
                        permissionManager.requestPermissions(requiredPermissions, activity)
                    }
                    .show()
        } else {
            savePath(createExternalPath())
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val granted = permissions
                .filterIndexed { index, _ -> grantResults[index] == PackageManager.PERMISSION_GRANTED }
                .any { it == Manifest.permission.WRITE_EXTERNAL_STORAGE }
        if (granted) {
            viewModel.saveMapCachePath(createExternalPath())
            router.pushController(RouterTransaction.with(HomeController()))
        }
    }

    private fun savePath(path: String) {
        viewModel.saveMapCachePath(path)
        router.setRoot(RouterTransaction.with(HomeController()))
    }

    private fun createInternalPath() = context.filesDir.absolutePath

    private fun createExternalPath() = File(context.getExternalFilesDir(null), "map-cache").absolutePath


}