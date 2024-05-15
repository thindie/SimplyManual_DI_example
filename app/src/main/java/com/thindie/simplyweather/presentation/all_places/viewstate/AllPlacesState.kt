package com.thindie.simplyweather.presentation.all_places.viewstate

import com.thindie.simplyweather.domain.DailyForecast
import com.thindie.simplyweather.presentation.TransitionState

data class AllPlacesState(
    val transitionState: TransitionState = TransitionState.None,
    val forecast: DailyForecast? = null
)
