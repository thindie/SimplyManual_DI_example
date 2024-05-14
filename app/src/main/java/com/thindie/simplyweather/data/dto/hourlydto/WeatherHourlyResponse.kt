package com.thindie.simplyweather.data.dto.hourlydto

import kotlinx.serialization.Serializable

@Suppress("ConstructorParameterNaming")
@Serializable
data class WeatherHourlyResponse(
    val elevation: Double,
    val generationtime_ms: Double,
    val hourly: Hourly,
    val hourly_units: HourlyUnits,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val timezone_abbreviation: String,
    val utc_offset_seconds: Int,
)