package com.devindi.wallpaper.misc

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.support.annotation.RequiresApi
import com.bluelinelabs.conductor.Controller

private const val REQUEST_CODE = 42

fun createPermissionManager(): PermissionManager {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PermissionManagerApiMImpl()
    } else {
        PermissionManagerImpl()
    }
}

interface PermissionManager {
    /**
     * Check is permissions granted.
     * @return list of denied permissions
     */
    fun checkPermissions(permissions: List<String>, resolver: Activity): List<String>

    /**
     * Requests provided [permissions]
     */
    fun requestPermissions(permissions: List<String>, resolver: Controller)
}

class PermissionManagerImpl:PermissionManager {

    override fun checkPermissions(permissions: List<String>, resolver: Activity): List<String> {
        return emptyList()
    }

    override fun requestPermissions(permissions: List<String>, resolver: Controller) {
        throw UnsupportedOperationException()
    }
}

@RequiresApi(Build.VERSION_CODES.M)
class PermissionManagerApiMImpl : PermissionManager {

    override fun checkPermissions(permissions: List<String>, resolver: Activity): List<String> {
        return permissions
                .filter { resolver.checkSelfPermission(it) == PackageManager.PERMISSION_DENIED }
    }

    override fun requestPermissions(permissions: List<String>, resolver: Controller) {
        resolver.requestPermissions(permissions.toTypedArray(), REQUEST_CODE)
    }
}
