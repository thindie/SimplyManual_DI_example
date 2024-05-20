package com.thindie.simplyweather.data.dto.dailydto

import kotlinx.serialization.Serializable

@Suppress("ConstructorParameterNaming")
@Serializable
data class WeatherDailyResponse(
    val daily: Daily,
    val daily_units: DailyUnits,
    val elevation: Double,
    val generationtime_ms: Double,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val timezone_abbreviation: String,
    val utc_offset_seconds: Int,
)
