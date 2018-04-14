package com.devindi.wallpaper.misc

import android.content.Context
import android.content.pm.PackageManager
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import timber.log.Timber

/**
 * Reports about analytic events, errors etc
 */
interface ReportManager {

    fun init(context: Context)

    fun reportError(error: Throwable)

    fun reportEvent(event: Any)
}

class FabricReportManager : ReportManager {
    override fun init(context: Context) {
        try {
            val info = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            val fabricKey = info.metaData.getString("io.fabric.ApiKey")
            if (!fabricKey.isNullOrEmpty() && fabricKey != "null") {
                Timber.d("Initializing fabric")
                Fabric.with(context, Crashlytics())
            }
        } catch (e: PackageManager.NameNotFoundException) {
            return
        }
    }

    override fun reportError(error: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun reportEvent(event: Any) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}


