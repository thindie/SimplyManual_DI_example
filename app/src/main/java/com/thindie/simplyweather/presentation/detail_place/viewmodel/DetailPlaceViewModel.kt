package com.thindie.simplyweather.presentation.detail_place.viewmodel

import android.util.Log
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
                _state.update {
                    it?.copy(
                        isHourlyWeatherRequested = !it.isHourlyWeatherRequested
                    )
                        ?: DetailScreenState(
                            isHourlyWeatherRequested = false
                        )
                }
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
                _state.update {
                    it?.copy(
                        isChangeTitleResumed = !it.isChangeTitleResumed
                    )
                        ?: DetailScreenState(
                            isChangeTitleResumed = false
                        )
                }
            }

            DetailPlaceScreenEvent.TriggerDropDownMenu -> {
                Log.d("SERVICE_TAG", "triggered")
                _state.update {
                    it?.copy(
                        isDropDownResumed = !it.isDropDownResumed
                    )
                }
            }
        }
    }
}
