package com.thindie.simplyweather.data.dto.currentdto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("current_units")
data class CurrentUnits(
    @SerialName("apparent_temperature")
    val apparentTemperature: String,
    @SerialName("interval")
    val interval: String,
    @SerialName("is_day")
    val isDay: String,
    @SerialName("precipitation")
    val precipitation: String,
    @SerialName("rain")
    val rain: String,
    @SerialName("relative_humidity_2m")
    val relativeHumidity2m: String,
    @SerialName("snowfall")
    val snowfall: String,
    @SerialName("time")
    val time: String,
    @SerialName("weather_code")
    val weatherCode: String,
    @SerialName("wind_direction_10m")
    val windDirection10m: String,
    @SerialName("wind_gusts_10m")
    val windGusts10m: String,
    @SerialName("wind_speed_10m")
    val windSpeed10m: String,
)