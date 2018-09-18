package com.devindi.wallpaper.search

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.devindi.wallpaper.model.places.GoogleApiErrorHandler

class SearchViewModel(
    private val manager: SearchManager,
    googleApiErrorHandler: GoogleApiErrorHandler
) : ViewModel() {

    val googleErrorData = googleApiErrorHandler.errorData

    fun requestSuggests(query: String) {
        manager.requestSuggests(query)
    }

    fun suggests(): MutableLiveData<List<PlaceSuggest>> {
        return manager.suggests
    }

    fun requestPlace(suggest: PlaceSuggest) {
        suggest.id?.let {
            manager.requestPlace(it)
        }
    }

    fun place(): MutableLiveData<Place> {
        return manager.place
    }
}
