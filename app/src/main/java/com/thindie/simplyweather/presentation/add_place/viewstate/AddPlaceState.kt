package com.thindie.simplyweather.presentation.add_place.viewstate

import com.thindie.simplyweather.domain.WeatherPlacePossibility
import com.thindie.simplyweather.presentation.TransitionState

data class AddPlaceState(
    val transitionState: TransitionState = TransitionState.None,
    val detectionPlaceTransitionState: TransitionState = TransitionState.None,
    val weatherPlacePossibility: List<WeatherPlacePossibility> = emptyList(),
    val titleError: AddPlaceError? = null,
    val latitudeError: AddPlaceError? = null,
    val longitudeError: AddPlaceError? = null,
    val isAddDialogResumed: Boolean = false,
    val placeTitle: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val addPlaceError: AddPlaceError? = null,
    val addPlaceSuccess: AddPlaceSuccess? = null,
) {
    val isInputFieldsIsNotError = titleError == null && latitudeError == null && longitudeError == null
}