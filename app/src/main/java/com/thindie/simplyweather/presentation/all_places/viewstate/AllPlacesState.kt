package com.thindie.simplyweather.presentation.all_places.viewstate

import com.thindie.simplyweather.domain.CurrentWeather
import com.thindie.simplyweather.domain.PrecipitationTime
import com.thindie.simplyweather.presentation.TransitionState

data class AllPlacesState(
    val transitionState: TransitionState = TransitionState.Loading,
    val expectedPrecipitationEvents: List<PrecipitationTime> = emptyList(),
    val forecast: Map<String, CurrentWeather> = emptyMap(),
)
