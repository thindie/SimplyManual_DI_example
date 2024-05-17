package com.thindie.simplyweather.domain

data class DailyForecast(
    val latitude: Float,
    val longitude: Float,
    val weatherCode: Int,
    val sunset: String,
    val sunrise: String,
    val apparentTempMax: Double,
    val apparentTempMin: Double,
    val precipitationHours: Double,
    val precipitationProbabilityMax: Int,
    val precipitationSum: Double,
    val rainSum: Double,
    val showersSum: Double,
    val snowfallSum: Double,
    val time: String,
    val uvIndexMax: Double,
    val windDirection10mDominant: Int,
    val windGusts10mMax: Double,
    val windSpeed10mMax: Double,
)