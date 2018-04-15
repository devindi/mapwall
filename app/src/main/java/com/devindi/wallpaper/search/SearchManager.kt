package com.devindi.wallpaper.search

import android.arch.lifecycle.MutableLiveData
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.Places
import timber.log.Timber
import com.google.android.gms.location.places.Place as GooglePlace

class SearchManager(private val apiClient: GoogleApiClient) {

    private val filter = AutocompleteFilter.Builder()
            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_REGIONS)
            .build()

    val suggests = MutableLiveData<List<PlaceSuggest>>()
    val place = MutableLiveData<Place>()

    fun requestSuggests(query: String) {
        Places.GeoDataApi.getAutocompletePredictions(apiClient, query, null, filter)
                .setResultCallback {
                    if (it.status.isSuccess) {
                        suggests.value = it
                                .map { prediction ->
                                    Timber.d("Prediction item ${prediction.placeId} / ${prediction.placeTypes} / ${prediction.getFullText(null)} / ${prediction.getPrimaryText(null)} / ${prediction.getSecondaryText(null)} ")
                                    PlaceSuggest(prediction.getPrimaryText(null).toString(), prediction.getSecondaryText(null).toString(), prediction.placeId) }
                    }
                    it.release()
                }
    }

    fun requestPlace(placeId: String) {
        Places.GeoDataApi.getPlaceById(apiClient, placeId).setResultCallback {
            if (it.status.isSuccess) {
                it.firstOrNull()?.let { googlePlace ->
                    Timber.d("Posting new place ${googlePlace.latLng}")
                    place.value = Place(googlePlace.latLng.latitude, googlePlace.latLng.longitude)
                }
            }
            it.release()
        }
    }
}