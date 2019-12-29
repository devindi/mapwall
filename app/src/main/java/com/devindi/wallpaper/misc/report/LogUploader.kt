package com.devindi.wallpaper.misc.report

import android.util.Log
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.io.File


private interface BulkLog {

    @POST("/")
    fun log(@Query("token") token: String, @Query("type") type: String, @Body body: RequestBody): Call<ResponseBody>
}

class LogUploader {

    private val logzToken = "YOUR_TOKEN_HERE"
    private val logger: BulkLog = Retrofit.Builder().baseUrl("https://listener.logz.io:8071").build().create(BulkLog::class.java)

    fun upload(file: File): Boolean {
        val body = RequestBody.create(MediaType.get("application/json"), file)
        val response = logger.log(logzToken, "mapwall", body).execute()
        Log.d("LogWriter", "response: $response")
        return true
    }
}