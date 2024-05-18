package com.thindie.simplyweather.presentation.detail_place.event

import com.thindie.simplyweather.domain.DailyForecast

sealed class DetailPlaceScreenEvent {
    data object OnBackClick : DetailPlaceScreenEvent()
    data class ChangePlaceTitle(val title: String, val latitude: String, val longitude: String) :
        DetailPlaceScreenEvent()
    data class DeletePlace(val latitude: String, val longitude: String) : DetailPlaceScreenEvent()
    data class RequestHourlyForecast(val dailyForecast: DailyForecast) : DetailPlaceScreenEvent()
    data object RequestDetailForecast : DetailPlaceScreenEvent()
}
