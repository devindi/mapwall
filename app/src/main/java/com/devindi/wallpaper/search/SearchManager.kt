package com.devindi.wallpaper.search

import android.arch.lifecycle.MutableLiveData
import com.devindi.wallpaper.model.places.GoogleApiErrorHandler
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.Places
import com.google.android.gms.location.places.Place as GooglePlace

class SearchManager(private val apiClient: GoogleApiClient, private val googleApiErrorHandler: GoogleApiErrorHandler) {

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
                                    PlaceSuggest(
                                            prediction.getPrimaryText(null).toString(),
                                            prediction.getSecondaryText(null).toString(),
                                            prediction.placeId)
                                }
                    } else {
                        googleApiErrorHandler.onErrorStatus(it.status)
                    }
                    it.release()
                }
    }

    fun requestPlace(placeId: String) {
        Places.GeoDataApi.getPlaceById(apiClient, placeId).setResultCallback {
            if (it.status.isSuccess) {
                it.firstOrNull()?.let { googlePlace ->
                    place.value = Place(googlePlace.latLng.latitude, googlePlace.latLng.longitude)
                }
            } else {
                googleApiErrorHandler.onErrorStatus(it.status)
            }
            it.release()
        }
    }
}
