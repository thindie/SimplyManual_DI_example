package com.thindie.simplyweather.domain

import androidx.compose.runtime.Stable

@Stable
data class HourlyForecast(
    val apparentTemperature: Double,
    val precipitation: Double,
    val rain: Double,
    val showers: Double,
    val snowfall: Double,
    val temperature2m: Double,
    val time: String,
    val visibility: Double,
    val weatherCode: Int,
    val windGusts10m: Double,
    val windSpeed10m: Double,
)