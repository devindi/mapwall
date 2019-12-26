package com.devindi.wallpaper.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.devindi.wallpaper.model.places.GoogleApiErrorHandler
import com.devindi.wallpaper.search.Place as MapwallPlace
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.*
import com.google.android.gms.location.places.Place
import com.google.android.gms.maps.model.LatLng
import com.nhaarman.mockito_kotlin.*
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

/**
 * author   : Eugene Dudnik
 * date     : 08/4/18
 * e-mail   : esdudnik@gmail.com
 */
class SearchManagerTest {

    private val suggestsQuery = "suggestsQuery"
    private val placeId = "placeId"
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

    @Test
    fun testRequestSuggestsWhenNotSuccess() {
        whenever(mockGeoDataApi.getAutocompletePredictions(mockApiClientApi, suggestsQuery, null, filter))
                .thenReturn(mockAutocompletePredictionPendingResult)
        whenever(mockAutocompletePredictionBuffer.status).thenReturn(statusCanceled)

        searchManager.requestSuggests(suggestsQuery)
        verify(mockAutocompletePredictionPendingResult).setResultCallback(autocompletePredictionCallBackCaptor.capture())
        autocompletePredictionCallBackCaptor.firstValue.onResult(mockAutocompletePredictionBuffer)

        verify(mockGoogleApiErrorHandler).onErrorStatus(statusCanceled)
    }

    @Test
    fun testRequestSuggestsWhenSuccess() {
        val predictions: MutableList<AutocompletePrediction> = mutableListOf(
                TestAutocompletePrediction("testPlaceId1", "testPrimaryText1",
                        "testSecondaryText1"),
                TestAutocompletePrediction("testPlaceId2", "testPrimaryText2",
                        "testSecondaryText2")
        )

        whenever(mockGeoDataApi.getAutocompletePredictions(mockApiClientApi, suggestsQuery, null, filter))
                .thenReturn(mockAutocompletePredictionPendingResult)
        whenever(mockAutocompletePredictionBuffer.status).thenReturn(statusSuccess)
        whenever(mockAutocompletePredictionBuffer.iterator()).thenReturn(predictions.iterator())

        searchManager.requestSuggests(suggestsQuery)
        verify(mockAutocompletePredictionPendingResult).setResultCallback(autocompletePredictionCallBackCaptor.capture())
        autocompletePredictionCallBackCaptor.firstValue.onResult(mockAutocompletePredictionBuffer)

        val expectedSuggestsList = mutableListOf(
                PlaceSuggest(suggestsQuery, "testPrimaryText1", "testSecondaryText1", "testPlaceId1"),
                PlaceSuggest(suggestsQuery, "testPrimaryText2", "testSecondaryText2", "testPlaceId2")
        )

        assertArrayEquals(searchManager.suggests.value?.toTypedArray(), expectedSuggestsList.toTypedArray())
    }

    @Test
    fun testRequestPlaceWhenNotSuccess() {
        whenever(mockGeoDataApi.getPlaceById(mockApiClientApi, placeId))
                .thenReturn(mockPlaceBufferPendingResult)
        whenever(mockPlaceBuffer.status).thenReturn(statusCanceled)

        searchManager.requestPlace(placeId)
        verify(mockPlaceBufferPendingResult).setResultCallback(placeBufferCallBackCaptor.capture())
        placeBufferCallBackCaptor.firstValue.onResult(mockPlaceBuffer)

        verify(mockGoogleApiErrorHandler).onErrorStatus(statusCanceled)
    }

    @Test
    fun testRequestPlaceWhenSuccess() {
        val expectedPlace: MutableList<Place> = mutableListOf(
                TestPlace(LatLng(0.0, 0.0))
        )

        whenever(mockGeoDataApi.getPlaceById(mockApiClientApi, placeId))
                .thenReturn(mockPlaceBufferPendingResult)
        whenever(mockPlaceBuffer.status).thenReturn(statusSuccess)
        whenever(mockPlaceBuffer.iterator()).thenReturn(expectedPlace.iterator())

        searchManager.requestPlace(placeId)
        verify(mockPlaceBufferPendingResult).setResultCallback(placeBufferCallBackCaptor.capture())
        placeBufferCallBackCaptor.firstValue.onResult(mockPlaceBuffer)

        assertEquals(searchManager.place.value, MapwallPlace(0.0, 0.0))
    }
}
