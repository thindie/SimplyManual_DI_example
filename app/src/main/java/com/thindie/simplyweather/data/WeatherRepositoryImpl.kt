package com.thindie.simplyweather.data

import com.thindie.simplyweather.data.mappers.toCurrentWeather
import com.thindie.simplyweather.data.mappers.toDailyForecastList
import com.thindie.simplyweather.data.mappers.toWeeklyForecast
import com.thindie.simplyweather.domain.CurrentWeather
import com.thindie.simplyweather.domain.DailyForecast
import com.thindie.simplyweather.domain.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class WeatherRepositoryImpl(private val apiService: ApiService) : WeatherRepository {
    private val cacheDaily = MutableStateFlow<List<DailyForecast>>(emptyList())
    override fun observeWeather(): Flow<List<DailyForecast>> {
        return cacheDaily.filterNotNull()
    }

    override fun observeWeather(latitude: String, longitude: String): Flow<List<DailyForecast>> {
       return cacheDaily
            .map { list ->
                list.filter {
                    it.latitude.toString().take(5) == latitude
                            && it.longitude.toString().take(5) == longitude
                }
            }
    }

    override suspend fun fetchWeather(latitude: Float, longitude: Float) {
        val timeZone = java.util.TimeZone.getDefault().id
        val weather = apiService
            .getDailyWeather(
                latitude = latitude,
                longitude = longitude,
                timeZone = timeZone,
            ).daily
            .toWeeklyForecast(
                latitude = latitude,
                longitude = longitude
            )
            .toDailyForecastList()

        cacheDaily.update {
            weather
        }
    }

    override fun observeCurrentWeather(): Flow<List<CurrentWeather>> {
        return cacheDaily
            .map { cache ->
                if (cache.isNotEmpty()) {
                    val weather = cache[0]
                    val currentWeather =
                        apiService.getCurrentWeather(
                            latitude = weather.latitude,
                            longitude = weather.longitude,
                            timeZone = java.util.TimeZone.getDefault().id
                        )
                            .current
                            .toCurrentWeather(weather.latitude, weather.longitude)
                    listOf(currentWeather)
                } else {
                    null
                }
            }
            .filterNotNull()
    }
}
