package com.thindie.simplyweather.presentation.all_places.event

import com.thindie.simplyweather.domain.CurrentWeather

sealed class AllPlacesScreenEvent {
    data object OnClickBack : AllPlacesScreenEvent()
    data class RequestPlaceDetails(val weather: CurrentWeather) : AllPlacesScreenEvent()
    data object EnteringScreen : AllPlacesScreenEvent()
    data object RequestAddPlaceScreen : AllPlacesScreenEvent()
    data object EmptyForecast : AllPlacesScreenEvent()
    data object RequestStoredPlaces: AllPlacesScreenEvent()
}
