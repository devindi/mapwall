package com.devindi.wallpaper.search

import androidx.lifecycle.MutableLiveData
import com.devindi.wallpaper.model.places.GoogleApiErrorHandler
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.GeoDataApi

class SearchManager(
    private val geoDataApi: GeoDataApi,
    private val apiClient: GoogleApiClient,
    private val googleApiErrorHandler: GoogleApiErrorHandler
) {

    private val filter = AutocompleteFilter.Builder()
        .setTypeFilter(AutocompleteFilter.TYPE_FILTER_REGIONS)
        .build()

    val suggests = MutableLiveData<List<PlaceSuggest>>()
    val place = MutableLiveData<Place>()

    fun requestSuggests(query: String) {
        geoDataApi.getAutocompletePredictions(apiClient, query, null, filter)
            .setResultCallback {
                if (it.status.isSuccess) {
                    suggests.value = it
                        .map { prediction ->
                            PlaceSuggest(
                                query,
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
        geoDataApi.getPlaceById(apiClient, placeId).setResultCallback {
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
