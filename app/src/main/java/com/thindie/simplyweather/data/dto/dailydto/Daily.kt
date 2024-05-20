package com.thindie.simplyweather.data.dto.dailydto

import kotlinx.serialization.Serializable

@Suppress("ConstructorParameterNaming")
@Serializable
data class Daily(
    val weathercode: List<Int>,
    val sunset: List<String>,
    val sunrise: List<String>,
    val apparent_temperature_max: List<Double>,
    val apparent_temperature_min: List<Double>,
    val precipitation_hours: List<Double>,
    val precipitation_probability_max: List<Int>,
    val precipitation_sum: List<Double>,
    val rain_sum: List<Double>,
    val showers_sum: List<Double>,
    val snowfall_sum: List<Double>,
    val time: List<String>,
    val uv_index_max: List<Double>,
    val winddirection_10m_dominant: List<Int>,
    val windgusts_10m_max: List<Double>,
    val windspeed_10m_max: List<Double>,
)
