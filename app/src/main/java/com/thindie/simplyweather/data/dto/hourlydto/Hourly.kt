package com.thindie.simplyweather.data.dto.hourlydto

import kotlinx.serialization.Serializable

@Suppress("ConstructorParameterNaming")
@Serializable
data class Hourly(
    val apparent_temperature: List<Double>,
    val precipitation: List<Double>,
    val rain: List<Double>,
    val showers: List<Double>,
    val snowfall: List<Double>,
    val temperature_2m: List<Double>,
    val time: List<String>,
    val visibility: List<Double>,
    val weathercode: List<Int>,
    val windgusts_10m: List<Double>,
    val windspeed_10m: List<Double>,
)