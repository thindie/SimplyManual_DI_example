package com.thindie.simplyweather.data

import kotlinx.serialization.json.JsonArray
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceDetectionApiService {
    @GET("/search")
    suspend fun getCoordinates(
        @Query("city") city: String,
        @Query("format") format: String = "json",
    ): JsonArray
}