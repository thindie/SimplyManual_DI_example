package com.thindie.simplyweather.data

import com.thindie.simplyweather.data.dto.placedetectiondto.PlacesDetectionDtoItem
import com.thindie.simplyweather.data.mappers.toCurrentWeather
import com.thindie.simplyweather.data.mappers.toDailyForecastList
import com.thindie.simplyweather.data.mappers.toHourlyForecastList
import com.thindie.simplyweather.data.mappers.toWeatherPlacePossibility
import com.thindie.simplyweather.data.mappers.toWeeklyForecast
import com.thindie.simplyweather.domain.CurrentWeather
import com.thindie.simplyweather.domain.DailyForecast
import com.thindie.simplyweather.domain.HourlyForecast
import com.thindie.simplyweather.domain.WeatherPlacePossibility
import com.thindie.simplyweather.domain.WeatherRepository
import com.thindie.simplyweather.presentation.getTemporalAccessor
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

class WeatherRepositoryImpl(
    private val apiService: ApiService,
    private val placeDetectionApiService: PlaceDetectionApiService,
) : WeatherRepository {
    private val cacheDaily = MutableStateFlow<List<DailyForecast>>(emptyList())
    private val cacheHourly = MutableStateFlow<List<HourlyForecast>>(emptyList())
    override fun observeWeather(): Flow<List<DailyForecast>> {
        return cacheDaily.filterNotNull()
    }

    override fun observeWeather(latitude: String, longitude: String): Flow<List<DailyForecast>> {
        return cacheDaily
            .map { list ->
                list.filter {
                    it.latitude.toString() == latitude
                            && it.longitude.toString() == longitude
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

    override suspend fun deleteWeather(latitude: String, longitude: String) {

    }

    override suspend fun updateWeather(title: String, latitude: String, longitude: String) {
    }

    override fun observePlaceTitle(latitude: String, longitude: String): Flow<String> {
        return flow { emit("a") }
    }

    override suspend fun fetchHourlyWeather(
        latitude: String,
        longitude: String,
        dailyForecast: DailyForecast,
    ) {
        val timeZone = java.util.TimeZone.getDefault()
        val accessor = getTemporalAccessor(dailyForecast.sunrise)
        val accessorFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val hourly = apiService.getHourlyWeatherByDate(
            iso8601String = accessorFormatter.format(accessor),
            latitude = try {
                latitude.toFloat()
            } catch (_: Exception) {
                0f
            },
            longitude = try {
                longitude.toFloat()
            } catch (_: Exception) {
                0f
            },
            timeZone = timeZone.id,
        )
        cacheHourly.update {
            hourly.hourly.toHourlyForecastList()
        }
    }

    override suspend fun getWeatherPlacePossibilities(placeRequest: String): List<WeatherPlacePossibility> {
        return try {
            val placesResponse = placeDetectionApiService.getCoordinates(
                city = placeRequest
            )
            placesResponse
                .map {
                    Json.decodeFromJsonElement<PlacesDetectionDtoItem>(it)
                }
                .map(PlacesDetectionDtoItem::toWeatherPlacePossibility)
        } catch (_: Exception) {
            emptyList()
        }
    }

    override fun observeHourlyWeather(): Flow<List<HourlyForecast>> {
        return cacheHourly
    }
}

