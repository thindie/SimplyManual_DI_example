package com.thindie.simplyweather.domain

data class DailyForecast(
    val latitude: Float,
    val longitude: Float,
    val weatherCode: List<Int>,
    val sunset: List<String>,
    val sunrise: List<String>,
    val apparentTempMax: List<Double>,
    val apparentTempMin: List<Double>,
    val precipitationHours: List<Double>,
    val precipitationProbabilityMax: List<Int>,
    val precipitationSum: List<Double>,
    val rainSum: List<Double>,
    val showersSum: List<Double>,
    val snowfallSum: List<Double>,
    val time: List<String>,
    val uvIndexMax: List<Double>,
    val windDirection10mDominant: List<Int>,
    val windGusts10mMax: List<Double>,
    val windSpeed10mMax: List<Double>,
)