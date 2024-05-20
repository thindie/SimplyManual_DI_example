package com.thindie.simplyweather.data.dto.currentdto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("current")
data class Current(
    @SerialName("apparent_temperature")
    val apparentTemperature: Double,
    @SerialName("interval")
    val interval: Int,
    @SerialName("is_day")
    val isDay: Int,
    @SerialName("precipitation")
    val precipitation: Double,
    @SerialName("rain")
    val rain: Double,
    @SerialName("relative_humidity_2m")
    val relativeHumidity2m: Int,
    @SerialName("snowfall")
    val snowfall: Double,
    @SerialName("time")
    val time: String,
    @SerialName("weather_code")
    val weatherCode: Int,
    @SerialName("wind_direction_10m")
    val windDirection10m: Int,
    @SerialName("wind_gusts_10m")
    val windGusts10m: Double,
    @SerialName("wind_speed_10m")
    val windSpeed10m: Double,
)