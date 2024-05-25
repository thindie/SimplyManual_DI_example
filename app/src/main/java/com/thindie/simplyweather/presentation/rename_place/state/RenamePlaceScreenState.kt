package com.thindie.simplyweather.presentation.rename_place.state

import com.thindie.simplyweather.presentation.TransitionState

data class RenamePlaceScreenState(
    val renameTransitionState: TransitionState = TransitionState.None,
    val title: String = "",
    val isTitleSuccessfulRenamed: Boolean = false,
)