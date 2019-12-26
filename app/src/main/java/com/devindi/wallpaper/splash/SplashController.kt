package com.devindi.wallpaper.splash

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import androidx.lifecycle.Observer
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devindi.wallpaper.MainActivity
import com.devindi.wallpaper.R
import com.devindi.wallpaper.misc.BaseController
import com.devindi.wallpaper.misc.PermissionManager
import com.devindi.wallpaper.misc.ReportManager
import com.devindi.wallpaper.misc.inject
import com.devindi.wallpaper.model.analytics.ScreenEvent
import org.koin.android.viewmodel.ext.android.viewModel

class SplashController : BaseController() {

    private val permissionManager: PermissionManager by inject()
    private val viewModel: SplashViewModel by viewModel()
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
            (activity as MainActivity).openMainScreen()
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        val granted = permissions
            .filterIndexed { index, _ -> grantResults[index] == PackageManager.PERMISSION_GRANTED }
            .any { it == Manifest.permission.WRITE_EXTERNAL_STORAGE }
        if (granted) {
            viewModel.useExternalStorage()
            return
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
            .setMessage(R.string.request_storage_permission_message)
            .setPositiveButton("Internal") { _, _ ->
                viewModel.useInternalStorage()
            }
            .setNegativeButton("External") { _, _ ->
                val requiredPermissions = permissionManager.checkPermissions(
                    listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    activity)
                if (requiredPermissions.isEmpty()) {
                    viewModel.useExternalStorage()
                } else {
                    permissionManager.requestPermissions(requiredPermissions, this)
                }
            }
            .show()
    }
}
