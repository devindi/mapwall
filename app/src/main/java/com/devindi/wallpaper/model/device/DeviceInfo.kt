package com.devindi.wallpaper.model.device

/**
 * author   : Eugene Dudnik
 * date     : 9/14/18
 * e-mail   : esdudnik@gmail.com
 */

interface DeviceInfo {

    fun getWidth(): Int

    fun getHeight(): Int

    fun getVersion(): Int
}

class DeviceInfoProvider(private val displayInfo: DisplayInfo, private val osInfo: OsInfo) : DeviceInfo {

    override fun getWidth(): Int {
        return displayInfo.getWidth()
    }

    override fun getHeight(): Int {
        return displayInfo.getHeight()
    }

    override fun getVersion(): Int {
        return osInfo.getVersion()
    }

}
