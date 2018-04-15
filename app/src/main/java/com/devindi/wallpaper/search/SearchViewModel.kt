package com.devindi.wallpaper.search

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class SearchViewModel(private val manager: SearchManager): ViewModel() {

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