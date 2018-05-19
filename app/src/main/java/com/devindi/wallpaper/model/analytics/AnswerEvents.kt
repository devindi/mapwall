package com.devindi.wallpaper.model.analytics

import com.crashlytics.android.answers.ContentViewEvent
import com.crashlytics.android.answers.CustomEvent
import com.devindi.wallpaper.home.Target
import com.crashlytics.android.answers.SearchEvent as ImplSearchEvent

sealed class AnswerEvent<out T> {
    abstract fun toEvent(): T
}

class ScreenEvent(private val title: String): AnswerEvent<ContentViewEvent>() {
    override fun toEvent(): ContentViewEvent = ContentViewEvent()
            .putContentType("screen")
            .putContentName(title)
}

class SuccessSearchEvent(private val query: String): AnswerEvent<ImplSearchEvent>() {
    override fun toEvent(): ImplSearchEvent =
            ImplSearchEvent()
                    .putQuery(query)
                    .putCustomAttribute("success", "true")
}

class FailedSearchEvent(private val query: String): AnswerEvent<ImplSearchEvent>() {
    override fun toEvent(): ImplSearchEvent =
            ImplSearchEvent()
                    .putQuery(query)
                    .putCustomAttribute("success", "false")
}

class ChooseMapEvent(private val id: String): AnswerEvent<CustomEvent>() {
    override fun toEvent(): CustomEvent =
            CustomEvent("map select")
                    .putCustomAttribute("id", id)

}

class CreateWallpaperEvent(private val mapId: String, private val lat: Double, private val lon: Double, private val zoom: Number, private val target: Target): AnswerEvent<CustomEvent>() {
    override fun toEvent(): CustomEvent =
            CustomEvent("create wallpaper")
                    .putCustomAttribute("map", mapId)
                    .putCustomAttribute("lat", lat)
                    .putCustomAttribute("lon", lon)
                    .putCustomAttribute("zoom", zoom)
                    .putCustomAttribute("target", target.name)
}
