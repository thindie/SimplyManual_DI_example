package com.thindie.simplyweather.data

import app.cash.sqldelight.coroutines.asFlow
import com.thindie.simplyweather.data.mappers.toCurrentWeather
import com.thindie.simplyweather.data.mappers.toDailyForecastList
import com.thindie.simplyweather.data.mappers.toHourlyForecastList
import com.thindie.simplyweather.data.mappers.toWeeklyForecast
import com.thindie.simplyweather.database.WeatherDb
import com.thindie.simplyweather.domain.CurrentWeather
import com.thindie.simplyweather.domain.DailyForecast
import com.thindie.simplyweather.domain.HourlyForecast
import com.thindie.simplyweather.domain.PrecipitationTime
import com.thindie.simplyweather.domain.WeatherRepository
import com.thindie.simplyweather.presentation.getTemporalAccessor
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class WeatherRepositoryImpl(
    private val apiService: ApiService,
    private val weatherDb: WeatherDb,
) : WeatherRepository {
    private val cacheHourly = MutableStateFlow<List<HourlyForecast>>(emptyList())
    override fun observeWeather(): Flow<List<DailyForecast>> {
        return weatherDb.placetitleQueries
            .getAllPlaces()
            .asFlow()
            .map {
                it.executeAsList()
            }
            .map {
                it.flatMap { place ->
                    apiService.getDailyWeather(
                        latitude = place.latitude.toFloat(),
                        longitude = place.longitude.toFloat(),
                        timeZone = java.util.TimeZone.getDefault().id
                    )
                        .daily
                        .toWeeklyForecast(
                            latitude = place.latitude.toFloat(),
                            longitude = place.longitude.toFloat(),
                        ).toDailyForecastList()
                }
            }
            .catch { emit(emptyList()) }
    }

    override fun observeWeather(latitude: String, longitude: String): Flow<List<DailyForecast>> {
        return observeWeather()
            .map {
                it.filter {
                    it.latitude.toString() == latitude && it.longitude.toString() == longitude
                }
            }
    }

    override fun observeStoredPlaces(): Flow<List<String>> {
        return weatherDb.placetitleQueries
            .getAllPlaces()
            .asFlow()
            .map {
                it.executeAsList()
            }
            .map { list ->
                list.map { it.title }
            }
            .catch { emit(emptyList()) }
    }


    override fun observeCurrentWeather(): Flow<List<CurrentWeather>> {
        return weatherDb.placetitleQueries
            .getAllPlaces()
            .asFlow()
            .map {
                it.executeAsList()
            }
            .map {
                it.map { places ->
                    apiService.getCurrentWeather(
                        latitude = places.latitude.toFloat(),
                        longitude = places.longitude.toFloat(),
                        timeZone = java.util.TimeZone.getDefault().id
                    ).current
                        .toCurrentWeather(
                            latitude = places.latitude.toFloat(),
                            longitude = places.longitude.toFloat(),
                        )
                }
            }
            .catch { emit(emptyList()) }
    }

    override suspend fun deleteWeather(title: String) {
        weatherDb.placetitleQueries.deletePlaceByTitle(title)
    }

    override suspend fun updateWeather(title: String, latitude: Float, longitude: Float) {
        weatherDb.placetitleQueries.updatePlaceTitle(
            title, latitude.toString(), longitude.toString()
        )
    }

    override fun observePlaceTitle(latitude: String, longitude: String): Flow<String> {
        return weatherDb.placetitleQueries.getPlaceByCoordinates(latitude, longitude).asFlow()
            .map { it.executeAsOne() }
            .catch { emit("") }
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

    override fun observeHourlyWeather(): Flow<List<HourlyForecast>> {
        return cacheHourly
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeNearestPrecipitation(
        latitude: String,
        longitude: String,
    ): Flow<PrecipitationTime> {
        observeWeather(latitude, longitude)
            .flatMapLatest {
                val currentDayForecast = it.firstOrNull()
                if (currentDayForecast == null) emptyFlow() else flowOf(currentDayForecast)
            }
            .onEach {
                fetchHourlyWeather(latitude, longitude, it)
            }

        return cacheHourly.flatMapLatest {
            val firstPrecipitation = it.firstOrNull { it.precipitation > 0 }
            if (firstPrecipitation == null) emptyFlow() else flowOf(
                PrecipitationTime(
                    latitude = latitude, longitude = longitude, time = firstPrecipitation.time
                )
            )
        }
    }
}

