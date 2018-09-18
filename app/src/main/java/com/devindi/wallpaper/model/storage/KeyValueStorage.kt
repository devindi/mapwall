package com.devindi.wallpaper.model.storage

import android.content.SharedPreferences
import kotlin.reflect.KClass

interface KeyValueStorage {

    fun <T> save(key: String, value: T)

    fun <T : Any> read(key: String, clazz: KClass<T>, defaultValue: T? = null): T?
}

class SharedPreferencesStorage(private val impl: SharedPreferences) : KeyValueStorage {

    override fun <T> save(key: String, value: T) {
        when (value) {
            is String -> impl.edit { it.putString(key, value) }
            else -> throw UnsupportedOperationException()
        }
    }

    override fun <T : Any> read(key: String, clazz: KClass<T>, defaultValue: T?): T? {
        return when (clazz) {
            String::class -> impl.getString(key, defaultValue as? String) as T?
            Int::class -> impl.getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> impl.getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> impl.getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> impl.getLong(key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException()
        }
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }
}