package com.thindie.simplyweather.presentation.detail_place.viewstate

import com.thindie.simplyweather.domain.DailyForecast

data class DetailScreenState(
    val title: String,
    val weeklyForecast: List<DailyForecast>,
)