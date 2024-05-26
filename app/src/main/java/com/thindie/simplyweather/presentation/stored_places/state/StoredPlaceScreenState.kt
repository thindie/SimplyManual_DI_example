package com.thindie.simplyweather.presentation.stored_places.state

import com.thindie.simplyweather.presentation.TransitionState

data class StoredPlaceScreenState(
    val transitionState: TransitionState = TransitionState.None,
    val places: List<String> = emptyList(),
)