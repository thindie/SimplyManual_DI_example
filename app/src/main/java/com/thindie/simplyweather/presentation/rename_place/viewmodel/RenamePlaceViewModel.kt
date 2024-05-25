package com.thindie.simplyweather.presentation.rename_place.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thindie.simplyweather.domain.WeatherRepository
import com.thindie.simplyweather.presentation.TransitionState
import com.thindie.simplyweather.presentation.rename_place.event.RenamePlaceScreenEvent
import com.thindie.simplyweather.presentation.rename_place.state.RenamePlaceScreenState
import com.thindie.simplyweather.routing.AppRouter
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RenamePlaceViewModel(
    private val repository: WeatherRepository,
    private val latitude: String,
    private val longitude: String,
    private val routeFlow: MutableSharedFlow<AppRouter.RouteEvent>,
) : ViewModel() {
    private val _state = MutableStateFlow(RenamePlaceScreenState())
    val state = _state.filterNotNull()

    fun onEvent(event: RenamePlaceScreenEvent) {
        when (event) {
            RenamePlaceScreenEvent.ApplyRenameTitle -> {
                try {

                } catch (_: Exception) {

                }
            }

            RenamePlaceScreenEvent.ClearInputField -> {
                _state.update {
                    it.copy(
                        title = ""
                    )
                }
            }

            RenamePlaceScreenEvent.DismissSnack -> {
                _state.update {
                    it.copy(
                        isTitleSuccessfulRenamed = false
                    )
                }
            }

            RenamePlaceScreenEvent.OnBack -> {
                viewModelScope.launch {
                    routeFlow.emit(
                        AppRouter.RouteEvent.Back
                    )
                }
            }

            RenamePlaceScreenEvent.OnRequestRenameTitle -> {

            }

            is RenamePlaceScreenEvent.RenameTitle -> {
                _state.update {
                    it.copy(
                        title = event.title
                    )
                }
            }
        }
    }
}