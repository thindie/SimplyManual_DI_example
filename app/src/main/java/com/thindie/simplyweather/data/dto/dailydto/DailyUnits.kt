package com.thindie.simplyweather.data.dto.dailydto

import kotlinx.serialization.Serializable
@Suppress("ConstructorParameterNaming")
@Serializable
data class DailyUnits(
    val precipitation_hours: String,
    val precipitation_probability_max: String,
    val precipitation_sum: String,
    val rain_sum: String,
    val showers_sum: String,
    val snowfall_sum: String,
    val time: String,
    val uv_index_max: String,
    val winddirection_10m_dominant: String,
    val windgusts_10m_max: String,
    val windspeed_10m_max: String
)
