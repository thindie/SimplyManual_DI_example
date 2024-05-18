package com.thindie.simplyweather.presentation.detail_place.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thindie.simplyweather.domain.WeatherRepository
import com.thindie.simplyweather.presentation.detail_place.event.DetailPlaceScreenEvent
import com.thindie.simplyweather.presentation.detail_place.viewstate.DetailScreenState
import com.thindie.simplyweather.routing.AppRouter
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
                        event.title,
                        event.latitude,
                        event.longitude
                    )
                }

            }

            is DetailPlaceScreenEvent.DeletePlace -> {
                viewModelScope.launch {
                    repository.deleteWeather(
                        event.latitude,
                        event.longitude
                    )
                }
            }

            is DetailPlaceScreenEvent.RequestHourlyForecast -> {

            }

            DetailPlaceScreenEvent.RequestDetailForecast -> {
                repository
                    .observeWeather(
                        latitude, longitude
                    )
                    .onEach { weather ->
                        _state.update {
                            it?.copy(weeklyForecast = weather)
                                ?: DetailScreenState(title = "", weeklyForecast = weather)
                        }
                    }
                    .launchIn(viewModelScope)
            }
        }
    }
}