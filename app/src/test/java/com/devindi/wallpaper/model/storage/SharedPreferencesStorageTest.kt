package com.devindi.wallpaper.model.storage

import android.content.SharedPreferences
import android.preference.PreferenceManager
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
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

    private val stringKey = "stringKey"
    private val stringAnyValue = "stringAnyValue"
    private val stringDefaultValue = ""
    private val intKey = "intKey"
    private val intAnyValue = Integer.MAX_VALUE
    private val intFefaultvalue = 0
    private val booleanKey = "booleanKey"
    private val booleanAnyValue = true
    private val booleanDefaultValue = false
    private val floatKey = "floatKey"
    private val floatAnyValue = Float.MAX_VALUE
    private val floatDefaultValue = 0f
    private val longKey = "longKey"
    private val longAnyValue = Long.MAX_VALUE
    private val longDefaultValue = 0L

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
        keyValueStorage.save(stringKey, stringAnyValue)

        assertEquals(stringAnyValue, sharedPreferences.getString(stringKey, stringDefaultValue))
    }

    @Test
    fun readSavedBeforeStringValueReturnValue() {
        sharedPreferencesEditor.putString(stringKey, stringAnyValue).apply()

        assertEquals(stringAnyValue, keyValueStorage.read(stringKey, String::class))
    }

    @Test
    fun readSavedBeforeIntValueReturnValue() {
        sharedPreferencesEditor.putInt(intKey, intAnyValue).apply()

        assertEquals(intAnyValue, keyValueStorage.read(intKey, Int::class))
    }

    @Test
    fun readSavedBeforeBooleanValueReturnValue() {
        sharedPreferencesEditor.putBoolean(booleanKey, booleanAnyValue).apply()

        assertEquals(booleanAnyValue, keyValueStorage.read(booleanKey, Boolean::class))
    }

    @Test
    fun readSavedBeforeFloatValueReturnValue() {
        sharedPreferencesEditor.putFloat(floatKey, floatAnyValue).apply()

        assertEquals(floatAnyValue, keyValueStorage.read(floatKey, Float::class))
    }

    @Test
    fun readSavedBeforeLongValueReturnValue() {
        sharedPreferencesEditor.putLong(longKey, longAnyValue).apply()

        assertEquals(longAnyValue, keyValueStorage.read(longKey, Long::class))
    }

    @Test
    fun readNotExistingKeyReturnDefaultString() {
        assertEquals(keyValueStorage.read(stringKey, String::class, stringDefaultValue), stringDefaultValue)
    }

    @Test
    fun readNotExistingKeyReturnDefaultInt() {
        assertEquals(keyValueStorage.read(intKey, Int::class, intFefaultvalue), intFefaultvalue)
    }

    @Test
    fun readNotExistingKeyReturnDefaultBoolean() {
        assertEquals(keyValueStorage.read(booleanKey, Boolean::class, booleanDefaultValue), booleanDefaultValue)
    }

    @Test
    fun readNotExistingKeyReturnDefaultFloat() {
        assertEquals(keyValueStorage.read(floatKey, Float::class, floatDefaultValue), floatDefaultValue)
    }

    @Test
    fun readNotExistingKeyReturnDefaultLong() {
        assertEquals(keyValueStorage.read(longKey, Long::class, longDefaultValue), longDefaultValue)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun saveNewValueIncorrectTypeThrowException() {
        val value = 0
        keyValueStorage.save(stringKey, value)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun readIncorrectTypeKeyThrowException() {
        keyValueStorage.read(stringKey, Set::class)
    }

}
