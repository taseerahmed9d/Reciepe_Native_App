package com.example.reciepe_native_app.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal val appJson: Json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    explicitNulls = false
    encodeDefaults = true
}

fun createHttpClient(): HttpClient = HttpClient(createHttpClientEngine()) {
    install(ContentNegotiation) {
        json(appJson)
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 15_000
        connectTimeoutMillis = 10_000
        socketTimeoutMillis = 15_000
    }
    install(DefaultRequest) {
        url("https://www.themealdb.com/api/json/v1/1/")
        header(HttpHeaders.Accept, ContentType.Application.Json.toString())
    }
}
