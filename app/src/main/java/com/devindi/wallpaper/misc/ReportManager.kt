package com.devindi.wallpaper.misc

import android.content.Context
import android.content.pm.PackageManager
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.answers.Answers
import com.devindi.wallpaper.model.analytics.AnswerEvent
import com.devindi.wallpaper.model.analytics.ChooseMapEvent
import com.devindi.wallpaper.model.analytics.CreateWallpaperEvent
import com.devindi.wallpaper.model.analytics.FailedSearchEvent
import com.devindi.wallpaper.model.analytics.ScreenEvent
import com.devindi.wallpaper.model.analytics.SuccessSearchEvent
import io.fabric.sdk.android.Fabric
import timber.log.Timber

/**
 * Reports about analytic events, errors etc
 */
interface ReportManager {

    fun init(context: Context)

    fun reportError(error: Throwable)

    fun reportEvent(event: AnswerEvent<Any>)
}

class FabricReportManager : ReportManager {

    private var initialized = false

    override fun init(context: Context) {
        try {
            val info = context.packageManager.getApplicationInfo(
                context.packageName,
                PackageManager.GET_META_DATA)
            val fabricKey = info.metaData.getString("io.fabric.ApiKey")
            if (!fabricKey.isNullOrEmpty() && fabricKey != "null") {
                Timber.d("Initializing fabric")
                Fabric.with(context, Crashlytics())
                initialized = true
            }
        } catch (e: PackageManager.NameNotFoundException) {
            return
        }
    }

    override fun reportError(error: Throwable) {
        if (!initialized) {
            Timber.w("Skipping error $error")
            return
        }
        Crashlytics.logException(error)
    }

    override fun reportEvent(event: AnswerEvent<Any>) {
        if (!initialized) {
            Timber.w("Skipping event $event")
            return
        }
        Timber.d("Reporting to fabric $event")
        val answers = Answers.getInstance()
        when (event) {
            is ScreenEvent -> answers.logContentView(event.toEvent())
            is SuccessSearchEvent -> answers.logSearch(event.toEvent())
            is FailedSearchEvent -> answers.logSearch(event.toEvent())
            is ChooseMapEvent -> answers.logCustom(event.toEvent())
            is CreateWallpaperEvent -> answers.logCustom(event.toEvent())
        }
    }
}
