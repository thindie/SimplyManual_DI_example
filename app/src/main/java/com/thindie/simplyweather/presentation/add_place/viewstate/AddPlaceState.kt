package com.thindie.simplyweather.presentation.add_place.viewstate

import com.thindie.simplyweather.presentation.TransitionState

data class AddPlaceState(
    val transitionState: TransitionState = TransitionState.None,
    val titleError: AddPlaceError? = null,
    val latitudeError: AddPlaceError? = null,
    val longitudeError: AddPlaceError? = null,
    val isAddDialogResumed: Boolean = false,
    val placeTitle: String = "",
    val latitude: String = "",
    val longitude: String = ""
) {
    val isNotError = titleError == null && latitudeError == null && longitudeError == null
}