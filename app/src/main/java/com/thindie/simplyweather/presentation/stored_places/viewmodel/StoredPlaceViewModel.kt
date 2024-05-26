package com.thindie.simplyweather.presentation.stored_places.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thindie.simplyweather.domain.WeatherRepository
import com.thindie.simplyweather.presentation.TransitionState
import com.thindie.simplyweather.presentation.stored_places.event.StoredPlacesEvent
import com.thindie.simplyweather.presentation.stored_places.state.StoredPlaceScreenState
import com.thindie.simplyweather.routing.AppRouter
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StoredPlaceViewModel(
    private val repository: WeatherRepository,
    private val routeFlow: MutableSharedFlow<AppRouter.RouteEvent>,
) : ViewModel() {
    private val _state = MutableStateFlow(StoredPlaceScreenState())
    val state = _state.filterNotNull()

    fun onEvent(event: StoredPlacesEvent) {
        when (event) {
            StoredPlacesEvent.OnBack -> viewModelScope.launch {
                routeFlow.emit(AppRouter.RouteEvent.Back)
            }

            StoredPlacesEvent.RequestStoredPlaces -> repository
                .observeStoredPlaces()
                .onStart {
                    _state.update {
                        it.copy(
                            transitionState = TransitionState.Loading
                        )
                    }
                }
                .onEmpty {
                    _state.update {
                        it.copy(
                            transitionState = TransitionState.Error
                        )
                    }
                }
                .onEach { list ->
                    _state.update {
                        it.copy(
                            transitionState = TransitionState.Content,
                            places = list
                        )
                    }
                }
                .launchIn(viewModelScope)
        }
    }
}