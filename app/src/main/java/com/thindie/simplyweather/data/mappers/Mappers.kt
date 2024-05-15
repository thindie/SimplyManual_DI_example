package com.thindie.simplyweather.data.mappers

import com.thindie.simplyweather.data.dto.dailydto.Daily
import com.thindie.simplyweather.domain.DailyForecast

fun Daily.toDailyForecast(
    latitude: Float,
    longitude: Float,
) =
    DailyForecast(
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