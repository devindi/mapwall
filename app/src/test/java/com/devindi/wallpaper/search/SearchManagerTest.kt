package com.devindi.wallpaper.search

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.devindi.wallpaper.model.places.GoogleApiErrorHandler
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.*
import com.google.android.gms.location.places.Place
import com.nhaarman.mockito_kotlin.*
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

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
    private val mockAutocompletePredictionPendingResult: PendingResult<AutocompletePredictionBuffer> = mock()
    private val mockPlaceBuffer: PlaceBuffer = mock()
    private val mockPlaceBufferPendingResult: PendingResult<PlaceBuffer> = mock()

    private val autocompletePredictionCallBackCaptor = argumentCaptor<ResultCallback<AutocompletePredictionBuffer>>()
    private val placeBufferCallBackCaptor = argumentCaptor<ResultCallback<PlaceBuffer>>()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {

    }

    @Test
    fun testRequestSuggestsWhenNotSuccess() {
        whenever(mockGeoDataApi.getAutocompletePredictions(mockApiClientApi, REQUEST_SUGGESTS_STRING, null, filter))
                .thenReturn(mockAutocompletePredictionPendingResult)
        whenever(mockAutocompletePredictionBuffer.status).thenReturn(statusCanceled)

        searchManager.requestSuggests(REQUEST_SUGGESTS_STRING)
        verify(mockAutocompletePredictionPendingResult).setResultCallback(autocompletePredictionCallBackCaptor.capture())
        autocompletePredictionCallBackCaptor.firstValue.onResult(mockAutocompletePredictionBuffer)

        verify(mockGoogleApiErrorHandler, times(1)).onErrorStatus(statusCanceled)
    }

    @Test
    fun testRequestSuggestsWhenSuccess() {
        val predictions : MutableList<AutocompletePrediction> = mutableListOf(

        )

        whenever(mockGeoDataApi.getAutocompletePredictions(mockApiClientApi, REQUEST_SUGGESTS_STRING, null, filter))
                .thenReturn(mockAutocompletePredictionPendingResult)
        whenever(mockAutocompletePredictionBuffer.status).thenReturn(statusSuccess)
        whenever(mockAutocompletePredictionBuffer.iterator()).thenReturn(predictions.iterator())

        searchManager.requestSuggests(REQUEST_SUGGESTS_STRING)
        verify(mockAutocompletePredictionPendingResult).setResultCallback(autocompletePredictionCallBackCaptor.capture())
        autocompletePredictionCallBackCaptor.firstValue.onResult(mockAutocompletePredictionBuffer)

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

    @Test
    fun testRequestPlaceWhenNotSuccess() {
        whenever(mockGeoDataApi.getPlaceById(mockApiClientApi, REQUEST_PLACE_STRING))
                .thenReturn(mockPlaceBufferPendingResult)
        whenever(mockPlaceBuffer.status).thenReturn(statusCanceled)

        searchManager.requestPlace(REQUEST_PLACE_STRING)
        verify(mockPlaceBufferPendingResult).setResultCallback(placeBufferCallBackCaptor.capture())
        placeBufferCallBackCaptor.firstValue.onResult(mockPlaceBuffer)

        verify(mockGoogleApiErrorHandler, times(1)).onErrorStatus(statusCanceled)
    }

    @Test
    fun testRequestPlaceWhenSuccess() {
        val expectedPlace : MutableList<Place> = mutableListOf(

        )

        whenever(mockGeoDataApi.getPlaceById(mockApiClientApi, REQUEST_PLACE_STRING))
                .thenReturn(mockPlaceBufferPendingResult)
        whenever(mockPlaceBuffer.status).thenReturn(statusSuccess)
        whenever(mockPlaceBuffer.iterator()).thenReturn(expectedPlace.iterator())

        searchManager.requestPlace(REQUEST_PLACE_STRING)
        verify(mockPlaceBufferPendingResult).setResultCallback(placeBufferCallBackCaptor.capture())
        placeBufferCallBackCaptor.firstValue.onResult(mockPlaceBuffer)

        assertEquals(searchManager.place.value, null)
    }
}
