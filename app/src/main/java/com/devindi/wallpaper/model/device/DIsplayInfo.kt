package com.devindi.wallpaper.model.device

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.graphics.Point
import android.view.WindowManager

/**
 * author   : Eugene Dudnik
 * date     : 9/14/18
 * e-mail   : esdudnik@gmail.com
 */
interface DisplayInfo {

    fun getWidth(): Int

    fun getHeight(): Int

}

class DisplayInfoProvider(private val context: Context) : DisplayInfo {

    val size = Point().also {
                (context.getSystemService(WINDOW_SERVICE) as WindowManager)
                        .defaultDisplay
                        .apply { getSize(it) }
            }

    override fun getWidth(): Int {
        return size.x
    }

    override fun getHeight(): Int {
        return size.y
    }

}
