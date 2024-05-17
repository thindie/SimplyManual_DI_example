package com.thindie.simplyweather.domain

import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun observeWeather(): Flow<List<DailyForecast>>
    fun observeWeather(latitude: String, longitude: String): Flow<List<DailyForecast>>
    suspend fun fetchWeather(latitude: Float, longitude: Float)
    fun observeCurrentWeather(): Flow<List<CurrentWeather>>
}