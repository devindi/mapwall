package com.devindi.wallpaper.model.device

import android.os.Build

/**
 * author   : Eugene Dudnik
 * date     : 9/14/18
 * e-mail   : esdudnik@gmail.com
 */
interface OsInfo{

    fun getVersion() : Int

}

class OsInfoProvider : OsInfo{

    override fun getVersion(): Int {
        return Build.VERSION.SDK_INT
    }
}
