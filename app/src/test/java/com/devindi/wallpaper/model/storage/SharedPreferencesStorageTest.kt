package com.devindi.wallpaper.model.storage

import android.content.SharedPreferences
import com.nhaarman.mockito_kotlin.*
//import org.junit.Assert.assertEquals
import org.junit.Test

import org.junit.Before
import org.mockito.ArgumentMatchers.anyString

/**
 * author   : Eugene Dudnik
 * date     : 9/11/18
 * e-mail   : esdudnik@gmail.com
 */
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
        mockSharedPreferences = mock()
        mockSharedPreferencesEditor = mock()
        whenever(mockSharedPreferences.edit()).thenReturn(mockSharedPreferencesEditor)
        whenever(mockSharedPreferencesEditor.commit()).thenReturn(true)

        mockKeyValueStorage = SharedPreferencesStorage(mockSharedPreferences)
    }

    @Test
    fun saveNewValueCorrectType() {
        mockKeyValueStorage.save(STRING_KEY, STRING_ANY_VALUE)

//        verify(mockSharedPreferencesEditor).putString(anyString(), anyString())
//        verify(mockSharedPreferencesEditor).commit()

//        eq(mockSharedPreferences.getString(STRING_KEY, STRING_DEFAULT_VALUE))

//        assert(mockSharedPreferences.contains(STRING_KEY))

//        verify(mockSharedPreferences).getString(STRING_KEY, "")

//        assertEquals(STRING_ANY_VALUE, mockSharedPreferences.getString(STRING_KEY, STRING_DEFAULT_VALUE))
    }

//    @Test
//    fun readSavedBeforeValueReturnValue() {
//        mockSharedPreferencesEditor.putString(STRING_KEY, STRING_ANY_VALUE).commit()
//        assertEquals(STRING_ANY_VALUE, mockKeyValueStorage.read(STRING_KEY, String::class))
//
//        mockSharedPreferencesEditor.putInt(INT_KEY, INT_ANY_VALUE).apply()
//        assertEquals(INT_ANY_VALUE, mockKeyValueStorage.read(INT_KEY, Int::class))
//
//        mockSharedPreferencesEditor.putBoolean(BOOLEAN_KEY, BOOLEAN_ANY_VALUE).apply()
//        assertEquals(BOOLEAN_ANY_VALUE, mockKeyValueStorage.read(BOOLEAN_KEY, Boolean::class))
//
//        mockSharedPreferencesEditor.putFloat(FLOAT_KEY, FLOAT_ANY_VALUE).apply()
//        assertEquals(FLOAT_ANY_VALUE, mockKeyValueStorage.read(FLOAT_KEY, Float::class))
//
//        mockSharedPreferencesEditor.putLong(LONG_KEY, LONG_ANY_VALUE).apply()
//        assertEquals(LONG_ANY_VALUE, mockKeyValueStorage.read(LONG_KEY, Long::class))
//
//    }
//
//    @Test
//    fun readNotExistingKeyReturnDefault() {
//        assertEquals(mockKeyValueStorage.read(STRING_KEY, String::class, STRING_DEFAULT_VALUE), STRING_DEFAULT_VALUE)
//
//        assertEquals(mockKeyValueStorage.read(INT_KEY, Int::class, INT_DEFAULT_VALUE), INT_DEFAULT_VALUE)
//
//        assertEquals(mockKeyValueStorage.read(BOOLEAN_KEY, Boolean::class, BOOLEAN_DEFAULT_VALUE), BOOLEAN_DEFAULT_VALUE)
//
//        assertEquals(mockKeyValueStorage.read(FLOAT_KEY, Float::class, FLOAT_DEFAULT_VALUE), FLOAT_ANY_VALUE)
//
//        assertEquals(mockKeyValueStorage.read(LONG_KEY, Long::class, LONG_DEFAULT_VALUE), LONG_ANY_VALUE)
//    }
//
//    @Test(expected = UnsupportedOperationException::class)
//    fun saveNewValueIncorrectTypeThrowException() {
//        val obj: Any
//        obj = 0
//        if (obj !is String)
//            mockKeyValueStorage.save(STRING_KEY, obj)
//    }
//
//    @Test(expected = UnsupportedOperationException::class)
//    fun readIncorrectTypeKeyThrowException() {
//        val obj: Any
//        obj = 0 to Double
//        if (obj !is String || obj !is Int || obj !is Boolean || obj !is Float || obj !is Long)
//            mockKeyValueStorage.read(STRING_KEY, String::class)
//    }

}