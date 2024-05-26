package com.thindie.simplyweather.domain

import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun observeWeather(): Flow<List<DailyForecast>>
    fun observeWeather(latitude: String, longitude: String): Flow<List<DailyForecast>>
    fun observeCurrentWeather(): Flow<List<CurrentWeather>>
    suspend fun deleteWeather(latitude: String, longitude: String)
    suspend fun updateWeather(title: String, latitude: Float, longitude: Float)

    fun observePlaceTitle(latitude: String, longitude: String): Flow<String>
    suspend fun fetchHourlyWeather(
        latitude: String,
        longitude: String,
        dailyForecast: DailyForecast,
    )

    suspend fun getWeatherPlacePossibilities(placeRequest: String): List<WeatherPlacePossibility>
    fun observeHourlyWeather(): Flow<List<HourlyForecast>>
}