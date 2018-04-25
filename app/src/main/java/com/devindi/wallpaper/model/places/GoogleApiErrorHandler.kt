package com.devindi.wallpaper.model.places

import com.devindi.wallpaper.misc.ReportManager
import com.devindi.wallpaper.misc.SingleLiveEvent
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.PlacesStatusCodes
import timber.log.Timber

class GoogleApiErrorHandler(private val reportManager: ReportManager): GoogleApiClient.OnConnectionFailedListener {

    val errorData = SingleLiveEvent<Throwable>()

    override fun onConnectionFailed(result: ConnectionResult) {
        Timber.e("Google api failed connection $result")
        val error = when (result.errorCode) {
            ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED -> PlayServicesOutdatedException()
            ConnectionResult.SERVICE_INVALID -> PlayServicesMissingException()
            else -> Exception("Google error $result")
        }
        onError(error)
    }

    fun onErrorStatus(status: Status) {
        Timber.e("Google api failed request $status")
        val error = when (status.statusCode) {
            PlacesStatusCodes.KEY_INVALID -> InvalidPlacesApiKeyException()
            else -> Exception("Google error $status")
        }
        onError(error)
    }

    private fun onError(error: Throwable) {
        reportManager.reportError(error)
        errorData.sendEvent(error)
    }
}
