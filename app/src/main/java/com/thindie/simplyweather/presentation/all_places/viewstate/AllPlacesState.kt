package com.thindie.simplyweather.presentation.all_places.viewstate

import com.thindie.simplyweather.domain.CurrentWeather
import com.thindie.simplyweather.presentation.TransitionState

data class AllPlacesState(
    val transitionState: TransitionState = TransitionState.Loading,
    val forecast: List<CurrentWeather> = emptyList(),
)
