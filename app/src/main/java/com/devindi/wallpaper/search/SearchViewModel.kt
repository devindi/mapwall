package com.devindi.wallpaper.search

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel

class SearchViewModel(private val manager: SearchManager): ViewModel() {

    fun requestSuggests(query: String) {
        manager.requestSuggests(query)
    }

    fun suggests(): LiveData<List<PlaceSuggest>> {
        return manager.suggests
    }
}