package com.example.opsctask1screens

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException



class HotspotInfoService {

    fun getHotspotInfo(locId: String, eBirdApiToken: String): String? {
        val client = OkHttpClient.Builder().build()

        val mediaType = "application/json".toMediaTypeOrNull()
        val body = RequestBody.create(mediaType, "")

        val url = "https://api.ebird.org/v2/ref/hotspot/info/$locId"
        val request = Request.Builder()
            .url(url)
            .method("GET", body)
            .addHeader("X-eBirdApiToken", eBirdApiToken)
            .build()

        try {
            val response = client.newCall(request).execute()
            return response.body?.string()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }
}

