package com.devindi.wallpaper.search

import com.devindi.wallpaper.model.places.GoogleApiErrorHandler
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.AutocompletePrediction
import com.google.android.gms.location.places.AutocompletePredictionBuffer
import com.google.android.gms.location.places.GeoDataApi
import com.nhaarman.mockito_kotlin.*
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * author   : Eugene Dudnik
 * date     : 08/4/18
 * e-mail   : esdudnik@gmail.com
 */
class SearchManagerTest {

    private val REQUEST_SUGGESTS_STRING = "REQUEST_SUGGESTS_STRING"
    private val REQUEST_PLACE_STRING = "REQUEST_PLACE_STRING"
    private val statusCanceled = Status.RESULT_CANCELED
    private val statusSuccess = Status.RESULT_SUCCESS

    private val mockGeoDataApi: GeoDataApi = mock()
    private val mockApiClientApi: GoogleApiClient = mock()
    private val mockGoogleApiErrorHandler: GoogleApiErrorHandler = mock()
    private val searchManager = SearchManager(mockGeoDataApi, mockApiClientApi, mockGoogleApiErrorHandler)

    private val filter = AutocompleteFilter.Builder()
            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_REGIONS)
            .build()

    private val mockAutocompletePredictionBuffer: AutocompletePredictionBuffer = mock()
    private val mockPendingResult: PendingResult<AutocompletePredictionBuffer> = mock()
    private val resultCallBackCaptor = argumentCaptor<ResultCallback<AutocompletePredictionBuffer>>()

    @Before
    fun setup() {

    }

    @Test
    fun testRequestSuggestWhenNotSuccess() {
        whenever(mockGeoDataApi.getAutocompletePredictions(mockApiClientApi, REQUEST_SUGGESTS_STRING, null, filter))
                .thenReturn(mockPendingResult)
        whenever(mockAutocompletePredictionBuffer.status).thenReturn(statusCanceled)

        searchManager.requestSuggests(REQUEST_SUGGESTS_STRING)
        verify(mockPendingResult).setResultCallback(resultCallBackCaptor.capture())
        resultCallBackCaptor.firstValue.onResult(mockAutocompletePredictionBuffer)

        verify(mockGoogleApiErrorHandler, times(1)).onErrorStatus(statusCanceled)
    }

    @Test
    fun testRequestSuggestWhenSuccess() {
        val predictions : MutableList<AutocompletePrediction> = mutableListOf(

        )

        whenever(mockGeoDataApi.getAutocompletePredictions(mockApiClientApi, REQUEST_SUGGESTS_STRING, null, filter))
                .thenReturn(mockPendingResult)
        whenever(mockAutocompletePredictionBuffer.status).thenReturn(statusSuccess)
        whenever(mockAutocompletePredictionBuffer.iterator()).thenReturn(predictions.iterator())

        searchManager.requestSuggests(REQUEST_SUGGESTS_STRING)
        verify(mockPendingResult).setResultCallback(resultCallBackCaptor.capture())
        resultCallBackCaptor.firstValue.onResult(mockAutocompletePredictionBuffer)

        val expectedSuggestsList = predictions
                .map { autocompletePrediction ->
                    PlaceSuggest(
                            REQUEST_SUGGESTS_STRING,
                            autocompletePrediction.getPrimaryText(null).toString(),
                            autocompletePrediction.getSecondaryText(null).toString(),
                            autocompletePrediction.placeId)
                }

        assertArrayEquals(searchManager.suggests.value?.toTypedArray(), expectedSuggestsList.toTypedArray())
    }
}
