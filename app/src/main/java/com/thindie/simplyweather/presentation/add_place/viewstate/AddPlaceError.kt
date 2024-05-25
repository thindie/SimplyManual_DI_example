package com.thindie.simplyweather.presentation.add_place.viewstate

sealed class AddPlaceError {
    data object PlaceValidation : AddPlaceError()
    data object LatitudeValidation : AddPlaceError()
    data object LongitudeValidation : AddPlaceError()
    data object RequestAddPlaceUnSuccess: AddPlaceError()
    data object RequestAutoFindUnSuccess: AddPlaceError()
    data object EmptyAutoFind: AddPlaceError()
}
