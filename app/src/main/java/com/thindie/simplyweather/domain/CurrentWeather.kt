package com.thindie.simplyweather.domain

data class CurrentWeather(
    val latitude: Float,
    val longitude: Float,
    val apparentTemperature: Double,
    val interval: Int,
    val isDay: Boolean,
    val precipitation: Double,
    val rain: Double,
    val relativeHumidity2m: Int,
    val snowfall: Double,
    val time: String,
    val weatherCode: Int,
    val windDirection10m: Int,
    val windGusts10m: Double,
    val windSpeed10m: Double,
)