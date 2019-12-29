package com.devindi.wallpaper.misc.report

import android.content.Context
import android.support.annotation.AnyThread
import android.util.Log
import org.json.JSONObject
import timber.log.Timber
import java.util.Date

/**
 * Writes logs to files and uploads them
 */
class RemoteTree(context: Context) : Timber.Tree() {

    private val writer = LogWriter(context)

    @AnyThread
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {

        val severity = when (priority) {
            Log.VERBOSE -> "vrbs"
            Log.DEBUG -> "debg"
            Log.INFO -> "info"
            Log.WARN -> "wrng"
            Log.ERROR -> "errr"
            Log.ASSERT -> "asrt"
            else -> "unkn"
        }
        val json = JSONObject("{@timestamp:\"${Date().time}\", level:\"$severity\", tag:\"$tag\", msg:\"$message\"}")
        writer.postLogEntry(json)
    }
}
