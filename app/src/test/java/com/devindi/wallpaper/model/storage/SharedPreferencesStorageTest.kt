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

    private val STRING_KEY = "STRING_KEY"
    private val STRING_ANY_VALUE = "STRING_ANY_VALUE"
    private val STRING_DEFAULT_VALUE = ""
    private val INT_KEY = "INT_KEY"
    private val INT_ANY_VALUE = Integer.MAX_VALUE
    private val INT_DEFAULT_VALUE = 0
    private val BOOLEAN_KEY = "BOOLEAN_KEY"
    private val BOOLEAN_ANY_VALUE = true
    private val BOOLEAN_DEFAULT_VALUE = false
    private val FLOAT_KEY = "FLOAT_KEY"
    private val FLOAT_ANY_VALUE = Float.MAX_VALUE
    private val FLOAT_DEFAULT_VALUE = 0f
    private val LONG_KEY = "LONG_KEY"
    private val LONG_ANY_VALUE = Long.MAX_VALUE
    private val LONG_DEFAULT_VALUE = 0L

    private lateinit var keyValueStorage: KeyValueStorage

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesEditor: SharedPreferences.Editor

    @Before
    fun setup() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RuntimeEnvironment.application.applicationContext)
        sharedPreferencesEditor = sharedPreferences.edit()

        keyValueStorage = SharedPreferencesStorage(sharedPreferences)
    }

    @Test
    fun saveNewValueCorrectType() {
        keyValueStorage.save(STRING_KEY, STRING_ANY_VALUE)

        assertEquals(STRING_ANY_VALUE, sharedPreferences.getString(STRING_KEY, STRING_DEFAULT_VALUE))
    }

    @Test
    fun readSavedBeforeStringValueReturnValue() {
        sharedPreferencesEditor.putString(STRING_KEY, STRING_ANY_VALUE).apply()

        assertEquals(STRING_ANY_VALUE, keyValueStorage.read(STRING_KEY, String::class))
    }

    @Test
    fun readSavedBeforeIntValueReturnValue() {
        sharedPreferencesEditor.putInt(INT_KEY, INT_ANY_VALUE).apply()

        assertEquals(INT_ANY_VALUE, keyValueStorage.read(INT_KEY, Int::class))
    }

    @Test
    fun readSavedBeforeBooleanValueReturnValue() {
        sharedPreferencesEditor.putBoolean(BOOLEAN_KEY, BOOLEAN_ANY_VALUE).apply()

        assertEquals(BOOLEAN_ANY_VALUE, keyValueStorage.read(BOOLEAN_KEY, Boolean::class))
    }

    @Test
    fun readSavedBeforeFloatValueReturnValue() {
        sharedPreferencesEditor.putFloat(FLOAT_KEY, FLOAT_ANY_VALUE).apply()

        assertEquals(FLOAT_ANY_VALUE, keyValueStorage.read(FLOAT_KEY, Float::class))
    }

    @Test
    fun readSavedBeforeLongValueReturnValue() {
        sharedPreferencesEditor.putLong(LONG_KEY, LONG_ANY_VALUE).apply()

        assertEquals(LONG_ANY_VALUE, keyValueStorage.read(LONG_KEY, Long::class))
    }

    @Test
    fun readNotExistingKeyReturnDefaultString() {
        assertEquals(keyValueStorage.read(STRING_KEY, String::class, STRING_DEFAULT_VALUE), STRING_DEFAULT_VALUE)
    }

    @Test
    fun readNotExistingKeyReturnDefaultInt() {
        assertEquals(keyValueStorage.read(INT_KEY, Int::class, INT_DEFAULT_VALUE), INT_DEFAULT_VALUE)
    }

    @Test
    fun readNotExistingKeyReturnDefaultBoolean() {
        assertEquals(keyValueStorage.read(BOOLEAN_KEY, Boolean::class, BOOLEAN_DEFAULT_VALUE), BOOLEAN_DEFAULT_VALUE)
    }

    @Test
    fun readNotExistingKeyReturnDefaultFloat() {
        assertEquals(keyValueStorage.read(FLOAT_KEY, Float::class, FLOAT_DEFAULT_VALUE), FLOAT_DEFAULT_VALUE)
    }

    @Test
    fun readNotExistingKeyReturnDefaultLong() {
        assertEquals(keyValueStorage.read(LONG_KEY, Long::class, LONG_DEFAULT_VALUE), LONG_DEFAULT_VALUE)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun saveNewValueIncorrectTypeThrowException() {
        val value = 0
        keyValueStorage.save(STRING_KEY, value)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun readIncorrectTypeKeyThrowException() {
        keyValueStorage.read(STRING_KEY, Set::class)
    }

}
