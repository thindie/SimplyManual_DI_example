package com.thindie.simplyweather.presentation.detail_place.viewstate

import com.thindie.simplyweather.domain.DailyForecast
import com.thindie.simplyweather.presentation.TransitionState

data class DetailScreenState(
    val transitionState: TransitionState = TransitionState.None,
    val titleTransitionState: TransitionState = TransitionState.None,
    val title: String = "",
    val weeklyForecast: List<DailyForecast> = emptyList(),
)