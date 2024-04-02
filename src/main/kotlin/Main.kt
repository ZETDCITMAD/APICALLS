package com.example.api

import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

fun makeHttpRequest() {
    val apiUrl = "http://172.16.13.12:5050/selfservice/viewtokens/37263841993"

    val client = OkHttpClient()
    val queryParams = mapOf(
        "page" to "1",
        "limit" to "10"
    )

    // Build URL with query parameters
    val urlBuilder = apiUrl.toHttpUrlOrNull()?.newBuilder() ?: run {
        println("Invalid URL: $apiUrl")
        return
    }
    for ((key, value) in queryParams) {
        urlBuilder.addQueryParameter(key, value)
    }
    val requestUrl = urlBuilder.build().toString()
    val requestBody = "{}".toRequestBody("application/json".toMediaTypeOrNull())
    val request = Request.Builder()
        .url(requestUrl)
        //.addHeader("Authorization", "Bearer $authToken")
        .post(requestBody)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                println("Response: $responseBody")
            } else {
                println("HTTP Error: ${response.code} - ${response.message}")
            }
        }

        override fun onFailure(call: Call, e: IOException) {
            println("Network Error: ${e.message}")
        }
    })
}

fun main() {
    // Make HTTP request
    makeHttpRequest()

    // Wait for response (asynchronous)
    Thread.sleep(5000) // Just for demonstration, you might use other methods for waiting in a real application
}