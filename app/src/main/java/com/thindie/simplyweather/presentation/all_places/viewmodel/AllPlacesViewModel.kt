package com.thindie.simplyweather.presentation.all_places.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thindie.simplyweather.domain.WeatherRepository
import com.thindie.simplyweather.presentation.TransitionState
import com.thindie.simplyweather.presentation.all_places.event.AllPlacesScreenEvent
import com.thindie.simplyweather.presentation.all_places.viewstate.AllPlacesState
import com.thindie.simplyweather.routing.AppRouter
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AllPlacesViewModel(
    private val repository: WeatherRepository,
    private val routeFlow: MutableSharedFlow<AppRouter.RouteEvent>,
) : ViewModel() {

    private val _screenState = MutableStateFlow(AllPlacesState())
    val screenState = _screenState.filterNotNull()
    fun onEvent(event: AllPlacesScreenEvent) {
        when (event) {
            AllPlacesScreenEvent.OnClickBack -> {
                viewModelScope.launch {
                    routeFlow.emit(AppRouter.RouteEvent.AddPlace)
                }
            }

            AllPlacesScreenEvent.EnteringScreen -> {
                viewModelScope.launch {
                    _screenState.update { it.copy(transitionState = TransitionState.Loading) }
                    try {
                        repository
                            .observeCurrentWeather()
                            .onEach { list ->
                                _screenState.update {
                                    it.copy(
                                        transitionState = if (list.isEmpty()) {
                                            TransitionState.None
                                        } else {
                                            TransitionState.Content
                                        },
                                        forecast = list
                                    )
                                }
                            }
                            .launchIn(viewModelScope)

                    } catch (e: Exception) {
                        _screenState.update {
                            it.copy(transitionState = TransitionState.Error)
                        }
                    }
                }
            }

            is AllPlacesScreenEvent.RequestPlaceDetails -> {
                viewModelScope.launch {
                    routeFlow.emit(
                        AppRouter.RouteEvent.DetailPlace(
                            latitude = event.forecast.latitude.toString(),
                            longitude = event.forecast.longitude.toString()
                        )
                    )
                }
            }

            AllPlacesScreenEvent.RequestAddPlaceScreen -> {
                viewModelScope.launch {
                    routeFlow.emit(AppRouter.RouteEvent.AddPlace)
                }
            }

            AllPlacesScreenEvent.EmptyForecast -> {
                _screenState.update {
                    it.copy(
                        transitionState = TransitionState.None
                    )
                }
            }
        }
    }
}
