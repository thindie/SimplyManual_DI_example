package com.thindie.simplyweather.data.mappers

import com.thindie.simplyweather.data.dto.currentdto.Current
import com.thindie.simplyweather.data.dto.dailydto.Daily
import com.thindie.simplyweather.domain.CurrentWeather
import com.thindie.simplyweather.domain.DailyForecast
import com.thindie.simplyweather.domain.WeeklyForecast

fun Daily.toWeeklyForecast(
    latitude: Float,
    longitude: Float,
) =
    WeeklyForecast(
        weatherCode = weathercode,
        sunset = sunset,
        sunrise = sunrise,
        apparentTempMax = apparent_temperature_max,
        apparentTempMin = apparent_temperature_min,
        precipitationHours = precipitation_hours,
        precipitationProbabilityMax = precipitation_probability_max,
        precipitationSum = precipitation_sum,
        rainSum = rain_sum,
        showersSum = showers_sum,
        snowfallSum = snowfall_sum,
        time = time,
        uvIndexMax = uv_index_max,
        windDirection10mDominant = winddirection_10m_dominant,
        windGusts10mMax = windgusts_10m_max,
        windSpeed10mMax = windspeed_10m_max,
        latitude = latitude,
        longitude = longitude
    )

fun WeeklyForecast.toDailyForecastList(): List<DailyForecast> {
    return buildList {
        repeat(sunset.size) {
            try {
                add(
                    DailyForecast(
                        latitude = latitude,
                        longitude = longitude,
                        weatherCode = weatherCode[it],
                        sunset = sunset[it],
                        sunrise = sunrise[it],
                        apparentTempMax = apparentTempMax[it],
                        apparentTempMin = apparentTempMin[it],
                        precipitationHours = precipitationHours[it],
                        precipitationProbabilityMax = precipitationProbabilityMax[it],
                        precipitationSum = precipitationSum[it],
                        rainSum = rainSum[it],
                        showersSum = showersSum[it],
                        snowfallSum = snowfallSum[it],
                        time = time[it],
                        uvIndexMax = uvIndexMax[it],
                        windDirection10mDominant = windDirection10mDominant[it],
                        windGusts10mMax = windGusts10mMax[it],
                        windSpeed10mMax = windSpeed10mMax[it]
                    )
                )
            } catch (_: Exception) {
                return emptyList()
            }
        }
    }
}

fun Current.toCurrentWeather(latitude: Float, longitude: Float) =
    CurrentWeather(
        latitude = latitude,
        longitude = longitude,
        apparentTemperature = apparentTemperature,
        interval = interval,
        isDay = isDay == 1,
        precipitation = precipitation,
        rain = rain,
        relativeHumidity2m = relativeHumidity2m,
        snowfall = snowfall,
        time = time,
        weatherCode = weatherCode,
        windDirection10m = windDirection10m,
        windGusts10m = windGusts10m,
        windSpeed10m = windSpeed10m

    )
