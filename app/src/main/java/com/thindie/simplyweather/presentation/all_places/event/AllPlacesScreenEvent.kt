package com.thindie.simplyweather.presentation.all_places.event

sealed class AllPlacesScreenEvent {
    data object OnClickBack: AllPlacesScreenEvent()
}