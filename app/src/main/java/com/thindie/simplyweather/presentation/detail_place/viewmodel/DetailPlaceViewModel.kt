package com.thindie.simplyweather.presentation.detail_place.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thindie.simplyweather.domain.WeatherRepository
import com.thindie.simplyweather.presentation.TransitionState
import com.thindie.simplyweather.presentation.detail_place.event.DetailPlaceScreenEvent
import com.thindie.simplyweather.presentation.detail_place.viewstate.DetailScreenState
import com.thindie.simplyweather.routing.AppRouter
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailPlaceViewModel(
    private val latitude: String,
    private val longitude: String,
    private val repository: WeatherRepository,
    private val routeFlow: MutableSharedFlow<AppRouter.RouteEvent>,
) : ViewModel() {
    private val _state = MutableStateFlow<DetailScreenState?>(null)
    val state = _state.filterNotNull()
    fun onEvent(event: DetailPlaceScreenEvent) {
        when (event) {
            DetailPlaceScreenEvent.OnBackClick -> {
                viewModelScope.launch {
                    routeFlow.emit(AppRouter.RouteEvent.AllPlaces)
                }
            }

            is DetailPlaceScreenEvent.ChangePlaceTitle -> {
                viewModelScope.launch {
                    repository.updateWeather(
                        title = event.title,
                        latitude = latitude,
                        longitude = longitude
                    )
                }

            }

            is DetailPlaceScreenEvent.DeletePlace -> {
                viewModelScope.launch {
                    repository.deleteWeather(
                        latitude = latitude,
                        longitude = longitude
                    )
                }
            }

            is DetailPlaceScreenEvent.RequestHourlyForecast -> {
                repository
                    .observeHourlyWeather()
                    .onStart {
                        repository.fetchHourlyWeather(
                            latitude = latitude,
                            longitude = longitude,
                            dailyForecast = event.dailyForecast
                        )
                        _state.update {
                            it?.copy(
                                isHourlyWeatherRequested = true,
                                hourlyTransitionState = TransitionState.Loading
                            )
                                ?: DetailScreenState(
                                    isHourlyWeatherRequested = false,
                                    hourlyTransitionState = TransitionState.Loading
                                )
                        }
                    }
                    .onEach { list ->
                        _state.update {
                            it?.copy(
                                hourlyList = list,
                                hourlyTransitionState = if (list.isEmpty()) {
                                    TransitionState.Error
                                } else {
                                    TransitionState.Content
                                }
                            )
                                ?: DetailScreenState(
                                    hourlyList = list,
                                    hourlyTransitionState = if (list.isEmpty()) {
                                        TransitionState.Error
                                    } else {
                                        TransitionState.Content
                                    }
                                )
                        }
                    }
                    .launchIn(viewModelScope)
            }

            DetailPlaceScreenEvent.RequestDetailForecast -> {
                repository
                    .observeWeather(
                        latitude = latitude,
                        longitude = longitude
                    )
                    .onStart {
                        _state.update {
                            DetailScreenState(
                                transitionState = TransitionState.Loading,
                                titleTransitionState = TransitionState.Loading
                            )
                        }
                    }
                    .onEach { weather ->
                        _state.update {
                            it?.copy(
                                weeklyForecast = weather,
                                transitionState = if (weather.isEmpty()) {
                                    TransitionState.None
                                } else {
                                    TransitionState.Content
                                }
                            )
                                ?: DetailScreenState(
                                    title = "",
                                    weeklyForecast = weather,
                                    transitionState = if (weather.isEmpty()) {
                                        TransitionState.None
                                    } else {
                                        TransitionState.Content
                                    }
                                )
                        }
                    }
                    .launchIn(viewModelScope)
            }

            DetailPlaceScreenEvent.TriggerChangeTitle -> {
                viewModelScope.launch {
                    routeFlow.emit(
                        AppRouter.RouteEvent.RenamePlace(
                            latitude = latitude, longitude = longitude
                        )
                    )
                }
            }

            DetailPlaceScreenEvent.TriggerDropDownMenu -> {
                _state.update {
                    it?.copy(
                        isDropDownResumed = !it.isDropDownResumed
                    )
                }
            }

            DetailPlaceScreenEvent.DismissHourlyForecast -> {
                _state.update {
                    it?.copy(
                        isHourlyWeatherRequested = false
                    )
                        ?: DetailScreenState(
                            isHourlyWeatherRequested = false
                        )
                }
            }
        }
    }
}
