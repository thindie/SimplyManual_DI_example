package com.thindie.simplyweather.presentation.all_places.event

import com.thindie.simplyweather.domain.WeeklyForecast

sealed class AllPlacesScreenEvent {
    data object OnClickBack: AllPlacesScreenEvent()
    data class RequestPlaceDetails(val forecast: WeeklyForecast): AllPlacesScreenEvent()
    data object EnteringScreen: AllPlacesScreenEvent()
    data object RequestAddPlaceScreen: AllPlacesScreenEvent()
    data object EmptyForecast: AllPlacesScreenEvent()
}