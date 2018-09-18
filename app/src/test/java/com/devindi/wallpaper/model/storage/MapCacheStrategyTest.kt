package com.devindi.wallpaper.model.storage

import android.content.Context
import android.os.Environment
import com.devindi.wallpaper.model.AndroidInfo
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowEnvironment
import java.io.File

/**
 * author   : Eugene Dudnik
 * date     : 9/15/18
 * e-mail   : esdudnik@gmail.com
 */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class MapCacheStrategyTest {

    private val INTERNAL_PATH = "INTERNAL_PATH"

    var mockAndroidInfo: AndroidInfo = mock()
    val mockContext: Context = mock()
    val mockInternalFile: File = mock()
    val mapCacheStrategy = MapCacheStrategy(mockContext, mockAndroidInfo)

    @Before
    fun setup() {
        whenever(mockContext.filesDir).thenReturn(mockInternalFile)
        whenever(mockInternalFile.absolutePath).thenReturn(INTERNAL_PATH)
    }

    @Test
    fun getDefaultCachePathBelowMExternalWritable() {
        whenever(mockAndroidInfo.version()).thenReturn(22)
        ShadowEnvironment.setExternalStorageState(Environment.MEDIA_MOUNTED)

        val expectedPath = mapCacheStrategy.createExternalPath()
        val realPath = mapCacheStrategy.defaultCachePath()

        assertEquals(realPath, expectedPath)
    }

    @Test
    fun getDefaultCachePathBelowMExternalNotWritable() {
        whenever(mockAndroidInfo.version()).thenReturn(22)
        ShadowEnvironment.setExternalStorageState(Environment.MEDIA_UNMOUNTED)

        val path = mapCacheStrategy.defaultCachePath()

        assertEquals(path, INTERNAL_PATH)
    }

    @Test
    fun getDefaultCachePathAboveMExternalWritable() {
        whenever(mockAndroidInfo.version()).thenReturn(24)
        ShadowEnvironment.setExternalStorageState(Environment.MEDIA_MOUNTED)

        val path = mapCacheStrategy.defaultCachePath()

        assertEquals(path, null)
    }

    @Test
    fun getDefaultCachePathAboveMExternalNotWritable() {
        whenever(mockAndroidInfo.version()).thenReturn(24)
        ShadowEnvironment.setExternalStorageState(Environment.MEDIA_UNMOUNTED)

        val path = mapCacheStrategy.defaultCachePath()

        assertEquals(path, INTERNAL_PATH)
    }
}
