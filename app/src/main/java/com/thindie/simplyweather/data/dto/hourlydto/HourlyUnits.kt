package com.thindie.simplyweather.data.dto.hourlydto

import kotlinx.serialization.Serializable

@Suppress("ConstructorParameterNaming")
@Serializable
data class HourlyUnits(
    val apparent_temperature: String,
    val precipitation: String,
    val rain: String,
    val showers: String,
    val snowfall: String,
    val temperature_2m: String,
    val time: String,
    val visibility: String,
    val weathercode: String,
    val windgusts_10m: String,
    val windspeed_10m: String,
)