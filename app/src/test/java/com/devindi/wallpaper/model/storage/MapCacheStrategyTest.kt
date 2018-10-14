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

    private val internalPath = "internalPath"
    private val externalPath = "externalPath"

    private lateinit var expectedCachePath: String
    private val mockAndroidInfo: AndroidInfo = mock()
    private val mockContext: Context = mock()
    private val mapCacheStrategy = MapCacheStrategy(mockContext, mockAndroidInfo)

    @Before
    fun setup() {
        val mockInternalFile: File = mock()
        val mockExternalFile = File(externalPath)

        whenever(mockContext.filesDir).thenReturn(mockInternalFile)
        whenever(mockInternalFile.absolutePath).thenReturn(internalPath)

        whenever(mockContext.getExternalFilesDir(null)).thenReturn(mockExternalFile)
        expectedCachePath = File(externalPath, "map-cache").absolutePath
    }

    @Test
    fun getDefaultCachePathBelowMExternalWritable() {
        whenever(mockAndroidInfo.version()).thenReturn(22)
        ShadowEnvironment.setExternalStorageState(Environment.MEDIA_MOUNTED)

        val realPath = mapCacheStrategy.defaultCachePath()

        assertEquals(realPath, expectedCachePath)
    }

    @Test
    fun getDefaultCachePathBelowMExternalNotWritable() {
        whenever(mockAndroidInfo.version()).thenReturn(22)
        ShadowEnvironment.setExternalStorageState(Environment.MEDIA_UNMOUNTED)

        val path = mapCacheStrategy.defaultCachePath()

        assertEquals(path, internalPath)
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

        assertEquals(path, internalPath)
    }

    @Test
    fun createInternalPathTest() {
        val path = mapCacheStrategy.createInternalPath()

        assertEquals(path, internalPath)
    }

    @Test
    fun createExternalPathTest() {
        val path = mapCacheStrategy.createExternalPath()

        assertEquals(path, expectedCachePath)
    }
}
