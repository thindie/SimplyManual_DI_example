package com.thindie.simplyweather.presentation.add_place.event

import com.thindie.simplyweather.domain.WeatherPlacePossibility

sealed class AddPlaceScreenEvent {
    data object EnterScreen : AddPlaceScreenEvent()
    data object OnClickBack : AddPlaceScreenEvent()
    data class LatitudeUpdate(val latitude: String) : AddPlaceScreenEvent()
    data class LongitudeUpdate(val longitude: String) : AddPlaceScreenEvent()
    data class PlaceNameUpdate(val place: String) : AddPlaceScreenEvent()
    data object AddPlaceApply : AddPlaceScreenEvent()
    data class RequestPlaceDetection(val placeTitle: String) : AddPlaceScreenEvent()
    data object DismissPlaceDetection : AddPlaceScreenEvent()
    data class ApplyPlaceDetection(val possibility: WeatherPlacePossibility) : AddPlaceScreenEvent()
    data object DismissSnack : AddPlaceScreenEvent()
    data object RequestAllPlacesScreen: AddPlaceScreenEvent()
}
