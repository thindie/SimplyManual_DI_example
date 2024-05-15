package com.thindie.simplyweather.data

import com.thindie.simplyweather.data.mappers.toDailyForecast
import com.thindie.simplyweather.domain.DailyForecast
import com.thindie.simplyweather.domain.WeatherRepository

class WeatherRepositoryImpl(private val apiService: ApiService) : WeatherRepository {
    override suspend fun getWeather(latitude: Float, longitude: Float): DailyForecast {
        val timeZone = java.util.TimeZone.getDefault().id
        return apiService
            .getDailyWeather(
                latitude = latitude,
                longitude = longitude,
                timeZone = timeZone,
            ).daily
            .toDailyForecast(
                latitude = latitude,
                longitude = longitude
            )
    }

}