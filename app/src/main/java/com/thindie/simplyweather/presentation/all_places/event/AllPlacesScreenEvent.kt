package com.thindie.simplyweather.presentation.all_places.event

import com.thindie.simplyweather.domain.DailyForecast

sealed class AllPlacesScreenEvent {
    data object OnClickBack: AllPlacesScreenEvent()
    data class RequestPlaceDetails(val forecast: DailyForecast): AllPlacesScreenEvent()
    data object EnteringScreen: AllPlacesScreenEvent()
}