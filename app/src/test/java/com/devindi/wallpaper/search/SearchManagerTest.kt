package com.devindi.wallpaper.search

import com.devindi.wallpaper.model.places.GoogleApiErrorHandler
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.AutocompletePredictionBuffer
import com.google.android.gms.location.places.GeoDataApi
import com.nhaarman.mockito_kotlin.*
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

    private val mockGeoDataApi: GeoDataApi = mock()
    private val mockApiClientApi: GoogleApiClient = mock()
    private val mockGoogleApiErrorHandler: GoogleApiErrorHandler = mock()
    private val searchManagerTest = SearchManager(mockGeoDataApi, mockApiClientApi, mockGoogleApiErrorHandler)

    private val filter = AutocompleteFilter.Builder()
            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_REGIONS)
            .build()

    private val mockAutocompletePredictionBuffer: AutocompletePredictionBuffer = mock()
    private val mockPendingResult: PendingResult<AutocompletePredictionBuffer> = mock()

    @Before
    fun setup() {

    }

    @Test
    fun testRequestSuggestWhenNotSuccess() {
        whenever(mockGeoDataApi.getAutocompletePredictions(mockApiClientApi, REQUEST_SUGGESTS_STRING, null, filter))
                .thenReturn(mockPendingResult)
        whenever(mockPendingResult.setResultCallback { autocompletePredictionBuffer: AutocompletePredictionBuffer -> Unit })
                .then {
                    mockAutocompletePredictionBuffer
                }
        whenever(mockAutocompletePredictionBuffer.status).thenReturn(statusCanceled)

        searchManagerTest.requestSuggests(REQUEST_SUGGESTS_STRING)

        verify(mockGoogleApiErrorHandler, times(1)).onErrorStatus(statusCanceled)
    }
}
