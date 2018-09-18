package com.devindi.wallpaper.model

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.WindowManager

/**
 * author   : Eugene Dudnik
 * date     : 9/14/18
 * e-mail   : esdudnik@gmail.com
 */

class DeviceInfo(private val context: Context) : DisplayInfo, AndroidInfo {

    private val screenSize by lazy { fetchScreenSize() }

    override fun screenWidth(): Int {
        return screenSize.x
    }

    override fun screenHeight(): Int {
        return screenSize.y
    }

    override fun version(): Int {
        return Build.VERSION.SDK_INT
    }

    private fun fetchScreenSize(): Point {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size
    }
}

interface AndroidInfo {

    fun version(): Int
}

interface DisplayInfo {

    fun screenWidth(): Int

    fun screenHeight(): Int
}
