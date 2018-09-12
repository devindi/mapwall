package com.devindi.wallpaper.model.storage

import android.content.SharedPreferences
import android.preference.PreferenceManager
import org.junit.Assert.assertEquals
import org.junit.Test

import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

/**
 * author   : Eugene Dudnik
 * date     : 9/11/18
 * e-mail   : esdudnik@gmail.com
 */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class SharedPreferencesStorageTest {

    private var STRING_KEY = "STRING_KEY"
    private var STRING_ANY_VALUE = "STRING_ANY_VALUE"
    private var STRING_DEFAULT_VALUE = ""
    private var INT_KEY = "INT_KEY"
    private var INT_ANY_VALUE = Integer.MAX_VALUE
    private var INT_DEFAULT_VALUE = 0
    private var BOOLEAN_KEY = "BOOLEAN_KEY"
    private var BOOLEAN_ANY_VALUE = true
    private var BOOLEAN_DEFAULT_VALUE = false
    private var FLOAT_KEY = "FLOAT_KEY"
    private var FLOAT_ANY_VALUE = Float.MAX_VALUE
    private var FLOAT_DEFAULT_VALUE = 0f
    private var LONG_KEY = "LONG_KEY"
    private var LONG_ANY_VALUE = Long.MAX_VALUE
    private var LONG_DEFAULT_VALUE = 0L

    private lateinit var mockKeyValueStorage: KeyValueStorage

    private lateinit var mockSharedPreferences: SharedPreferences
    private lateinit var mockSharedPreferencesEditor: SharedPreferences.Editor

    @Before
    fun initMocks() {
        mockSharedPreferences = PreferenceManager.getDefaultSharedPreferences(RuntimeEnvironment.application.applicationContext)
        mockSharedPreferencesEditor = mockSharedPreferences.edit()

        mockKeyValueStorage = SharedPreferencesStorage(mockSharedPreferences)
    }

    @Test
    fun saveNewValueCorrectType() {
        mockKeyValueStorage.save(STRING_KEY, STRING_ANY_VALUE)
        assertEquals(STRING_ANY_VALUE, mockSharedPreferences.getString(STRING_KEY, STRING_DEFAULT_VALUE))
    }

    @Test
    fun readSavedBeforeValueReturnValue() {
        mockSharedPreferencesEditor.putString(STRING_KEY, STRING_ANY_VALUE).commit()
        assertEquals(STRING_ANY_VALUE, mockKeyValueStorage.read(STRING_KEY, String::class))

        mockSharedPreferencesEditor.putInt(INT_KEY, INT_ANY_VALUE).apply()
        assertEquals(INT_ANY_VALUE, mockKeyValueStorage.read(INT_KEY, Int::class))

        mockSharedPreferencesEditor.putBoolean(BOOLEAN_KEY, BOOLEAN_ANY_VALUE).apply()
        assertEquals(BOOLEAN_ANY_VALUE, mockKeyValueStorage.read(BOOLEAN_KEY, Boolean::class))

        mockSharedPreferencesEditor.putFloat(FLOAT_KEY, FLOAT_ANY_VALUE).apply()
        assertEquals(FLOAT_ANY_VALUE, mockKeyValueStorage.read(FLOAT_KEY, Float::class))

        mockSharedPreferencesEditor.putLong(LONG_KEY, LONG_ANY_VALUE).apply()
        assertEquals(LONG_ANY_VALUE, mockKeyValueStorage.read(LONG_KEY, Long::class))

    }

    @Test
    fun readNotExistingKeyReturnDefault() {
        assertEquals(mockKeyValueStorage.read(STRING_KEY, String::class, STRING_DEFAULT_VALUE), STRING_DEFAULT_VALUE)

        assertEquals(mockKeyValueStorage.read(INT_KEY, Int::class, INT_DEFAULT_VALUE), INT_DEFAULT_VALUE)

        assertEquals(mockKeyValueStorage.read(BOOLEAN_KEY, Boolean::class, BOOLEAN_DEFAULT_VALUE), BOOLEAN_DEFAULT_VALUE)

        assertEquals(mockKeyValueStorage.read(FLOAT_KEY, Float::class, FLOAT_DEFAULT_VALUE), FLOAT_DEFAULT_VALUE)

        assertEquals(mockKeyValueStorage.read(LONG_KEY, Long::class, LONG_DEFAULT_VALUE), LONG_DEFAULT_VALUE)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun saveNewValueIncorrectTypeThrowException() {
        val value = 0
        mockKeyValueStorage.save(STRING_KEY, value)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun readIncorrectTypeKeyThrowException() {
        val value = HashSet<String>()
        mockSharedPreferencesEditor.putStringSet(STRING_KEY, value)
        mockKeyValueStorage.read(STRING_KEY, Set::class)
    }

}