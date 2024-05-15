package com.thindie.simplyweather.domain

interface WeatherRepository {
    suspend fun getWeather(latitude: Float, longitude: Float): DailyForecast
}