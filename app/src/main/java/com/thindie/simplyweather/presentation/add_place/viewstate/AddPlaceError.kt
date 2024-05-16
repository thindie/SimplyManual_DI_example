package com.thindie.simplyweather.presentation.add_place.viewstate

sealed class AddPlaceError {
    data object PlaceValidation : AddPlaceError()
    data object LatitudeValidation : AddPlaceError()
    data object LongitudeValidation : AddPlaceError()
}