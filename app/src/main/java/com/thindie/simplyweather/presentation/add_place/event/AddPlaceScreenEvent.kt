package com.thindie.simplyweather.presentation.add_place.event

sealed class AddPlaceScreenEvent {
    data object EnterScreen: AddPlaceScreenEvent()
    data object OnClickBack: AddPlaceScreenEvent()
    data class LatitudeUpdate(val latitude: String): AddPlaceScreenEvent()
    data class LongitudeUpdate(val longitude: String): AddPlaceScreenEvent()
    data class PlaceNameUpdate(val place: String): AddPlaceScreenEvent()
    data object AddPlaceApply: AddPlaceScreenEvent()
}
