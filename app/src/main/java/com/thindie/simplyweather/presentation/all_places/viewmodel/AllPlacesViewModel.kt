package com.thindie.simplyweather.presentation.all_places.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thindie.simplyweather.domain.WeatherRepository
import com.thindie.simplyweather.presentation.all_places.event.AllPlacesScreenEvent
import com.thindie.simplyweather.routing.AppRouter
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class AllPlacesViewModel(
    private val repository: WeatherRepository,
    private val routeFlow: MutableSharedFlow<AppRouter.RouteEvent>,
) : ViewModel() {
    fun onEvent(event: AllPlacesScreenEvent) {
        when (event) {
            AllPlacesScreenEvent.OnClickBack -> {
                viewModelScope.launch {
                    routeFlow.emit(AppRouter.RouteEvent.AddPlace)
                }
            }
        }
    }

}