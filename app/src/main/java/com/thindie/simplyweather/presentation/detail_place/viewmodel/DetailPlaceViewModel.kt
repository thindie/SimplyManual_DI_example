package com.thindie.simplyweather.presentation.detail_place.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thindie.simplyweather.domain.WeatherRepository
import com.thindie.simplyweather.presentation.detail_place.event.DetailPlaceScreenEvent
import com.thindie.simplyweather.routing.AppRouter
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class DetailPlaceViewModel(
    private val latitude: String,
    private val longitude: String,
    private val repository: WeatherRepository,
    private val routeFlow: MutableSharedFlow<AppRouter.RouteEvent>,
) : ViewModel() {
    fun onEvent(event: DetailPlaceScreenEvent) {
        when (event) {
            DetailPlaceScreenEvent.OnBackClick -> {
                viewModelScope.launch {
                    routeFlow.emit(AppRouter.RouteEvent.AllPlaces)
                }
            }
        }
    }
}