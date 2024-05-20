@file:Suppress("LongParameterList")

package com.thindie.simplyweather.data

import com.thindie.simplyweather.data.dto.currentdto.CurrentResponse
import com.thindie.simplyweather.data.dto.dailydto.WeatherDailyResponse
import com.thindie.simplyweather.data.dto.hourlydto.WeatherHourlyResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/v1/forecast")
    suspend fun getDailyWeather(
        @Query(PARAM_LATITUDE) latitude: Float,
        @Query(PARAM_LONGITUDE) longitude: Float,
        @Query(PARAM_DAILY, encoded = true) daily: String = NETWORK_QUERY,
        @Query(PARAM_WINDSPEED_UNIT) windSpeed: String = "ms",
        @Query(PARAM_TIMEZONE, encoded = true) timeZone: String,
        @Query(CURRENT_WEATHER) boolean: Boolean = true,
    ): WeatherDailyResponse

    @GET("/v1/forecast")
    suspend fun getHourlyWeather(
        @Query(PARAM_LATITUDE) latitude: Float,
        @Query(PARAM_LONGITUDE) longitude: Float,
        @Query(PARAM_HOURLY, encoded = true) hourly: String = HOURLY_NETWORK_QUERY,
        @Query(PARAM_WINDSPEED_UNIT) windSpeed: String = "ms",
        @Query(PARAM_TIMEZONE, encoded = true) timeZone: String,
        @Query(CURRENT_WEATHER) boolean: Boolean = true,
    ): WeatherHourlyResponse

    @GET("/v1/forecast")
    suspend fun getHourlyWeatherByDate(
        @Query(PARAM_START_DATE) iso8601String: String,
        @Query(PARAM_END_DATE) iso8601StringEnd: String = iso8601String,
        @Query(PARAM_LATITUDE) latitude: Float,
        @Query(PARAM_LONGITUDE) longitude: Float,
        @Query(PARAM_HOURLY, encoded = true) hourly: String = HOURLY_NETWORK_QUERY,
        @Query(PARAM_WINDSPEED_UNIT) windSpeed: String = "ms",
        @Query(PARAM_TIMEZONE, encoded = true) timeZone: String,
        @Query(CURRENT_WEATHER) boolean: Boolean = true,
    ): WeatherHourlyResponse

    @GET("/v1/forecast")
    suspend fun getCurrentWeather(
        @Query(PARAM_LATITUDE) latitude: Float,
        @Query(PARAM_LONGITUDE) longitude: Float,
        @Query(PARAM_CURRENT, encoded = true) current: String = CURRENT_NETWORK_QUERY,
        @Query(PARAM_WINDSPEED_UNIT) windSpeed: String = "ms",
        @Query(PARAM_TIMEZONE, encoded = true) timeZone: String,
    ): CurrentResponse
}


private const val PARAM_START_DATE = "start_date"
private const val PARAM_END_DATE = "end_date"
private const val PARAM_LONGITUDE = "longitude"
private const val PARAM_LATITUDE = "latitude"
private const val CURRENT_WEATHER = "current_weather"
private const val PARAM_WINDSPEED_UNIT = "windspeed_unit"
private const val PARAM_TIMEZONE = "timezone"
private const val PARAM_DAILY = "daily"
private const val PARAM_HOURLY = "hourly"
private const val PARAM_CURRENT = "current"
private const val NETWORK_QUERY =
    "weathercode,temperature_2m_max,temperature_2m_min," +
            "apparent_temperature_max,apparent_temperature_min,sunrise,sunset,uv_index_max,precipitation_sum," +
            "rain_sum,showers_sum,snowfall_sum,precipitation_hours,precipitation_probability_max,windspeed_10m_max," +
            "windgusts_10m_max,winddirection_10m_dominant"
private const val HOURLY_NETWORK_QUERY =
    "temperature_2m,apparent_temperature,precipitation," +
            "rain,showers,snowfall,weathercode,visibility,windspeed_10m" +
            ",windgusts_10m"

private const val CURRENT_NETWORK_QUERY =
    "relative_humidity_2m,apparent_temperature,is_day,precipitation,rain,snowfall,weather_code,wind_speed_10m,wind_direction_10m,wind_gusts_10m"