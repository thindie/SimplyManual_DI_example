package com.thindie.simplyweather.presentation

sealed class TransitionState {
    data object Loading: TransitionState()
    data object None: TransitionState()
    data object Content: TransitionState()
    data object Error: TransitionState()
}
