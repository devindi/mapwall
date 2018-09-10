package com.devindi.wallpaper.settings.model

interface SettingsField<T> {

    val key: String

    fun get(): T

    fun get(fallback: T): T

    fun set(value: T)
}
