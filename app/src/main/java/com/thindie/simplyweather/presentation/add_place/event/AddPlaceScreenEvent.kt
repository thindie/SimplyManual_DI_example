package com.thindie.simplyweather.presentation.add_place.event

sealed class AddPlaceScreenEvent {
    data object OnClickBack: AddPlaceScreenEvent()
}