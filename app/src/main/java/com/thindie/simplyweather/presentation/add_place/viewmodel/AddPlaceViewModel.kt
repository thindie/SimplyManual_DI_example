package com.thindie.simplyweather.presentation.add_place.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thindie.simplyweather.domain.WeatherRepository
import com.thindie.simplyweather.presentation.TransitionState
import com.thindie.simplyweather.presentation.add_place.event.AddPlaceScreenEvent
import com.thindie.simplyweather.presentation.add_place.viewstate.AddPlaceError
import com.thindie.simplyweather.presentation.add_place.viewstate.AddPlaceState
import com.thindie.simplyweather.routing.AppRouter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class AddPlaceViewModel(
    private val repository: WeatherRepository,
    private val routeFlow: MutableSharedFlow<AppRouter.RouteEvent>,
) : ViewModel() {

    private val _state = MutableStateFlow(AddPlaceState())
    val state = _state.filterNotNull()
    fun onEvent(event: AddPlaceScreenEvent) {
        when (event) {
            AddPlaceScreenEvent.OnClickBack -> {
                viewModelScope.launch {
                    routeFlow.emit(AppRouter.RouteEvent.Back)
                }
            }

            is AddPlaceScreenEvent.LatitudeUpdate -> {
                _state.update {
                    it.copy(
                        latitude = event.latitude,
                        latitudeError = null,
                    )
                }
            }

            is AddPlaceScreenEvent.LongitudeUpdate -> {
                _state.update {
                    it.copy(
                        longitude = event.longitude,
                        longitudeError = null,
                    )
                }
            }

            is AddPlaceScreenEvent.PlaceNameUpdate -> {
                _state.update {
                    it.copy(
                        placeTitle = event.place,
                        titleError = null,
                    )
                }
            }

            AddPlaceScreenEvent.AddPlaceApply -> {
                _state.update {
                    it.copy(
                        transitionState = TransitionState.Loading
                    )
                }
                viewModelScope.launch {
                    validateTitle()
                    validateLatitude()
                    validateLongitude()
                    if (_state.value.isNotError) {
                        try {
                            val weather =
                                repository.fetchWeather(
                                    latitude = getFloatFromString(_state.value.latitude),
                                    longitude = getFloatFromString(_state.value.longitude)
                                )
                        } catch (_: Exception) {
                            _state.update {
                                it.copy(
                                    transitionState = TransitionState.Error
                                )
                            }
                        }
                    }
                }.invokeOnCompletion {
                    _state.update {
                        it.copy(
                            transitionState = TransitionState.Content
                        )
                    }
                }
            }

            AddPlaceScreenEvent.EnterScreen -> {
                viewModelScope.launch {
                    _state.update {
                        delay(1.seconds)
                        it.copy(
                            transitionState = TransitionState.Content
                        )
                    }
                }
            }
        }
    }

    private fun validateLatitude() {
        try {
            getFloatFromString(_state.value.latitude)
        } catch (_: Exception) {
            _state.update {
                it.copy(
                    latitudeError = AddPlaceError.LatitudeValidation
                )
            }
        }
    }

    private fun validateLongitude() {
        try {
            getFloatFromString(_state.value.longitude)
        } catch (_: Exception) {
            _state.update {
                it.copy(
                    longitudeError = AddPlaceError.LongitudeValidation
                )
            }
        }
    }

    private fun validateTitle() {
        _state.value.placeTitle.ifEmpty {
            _state.update {
                it.copy(
                    titleError = AddPlaceError.PlaceValidation
                )
            }
        }
    }

    private fun getFloatFromString(string: String): Float {
        return string
            .trim()
            .take(5)
            .toFloat()
    }
}
