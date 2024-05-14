package com.thindie.simplyweather.presentation.add_place.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thindie.simplyweather.domain.WeatherRepository
import com.thindie.simplyweather.presentation.add_place.event.AddPlaceScreenEvent
import com.thindie.simplyweather.routing.AppRouter
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class AddPlaceViewModel(
    private val repository: WeatherRepository,
    private val routeFlow: MutableSharedFlow<AppRouter.RouteEvent>,
) : ViewModel() {
    fun onEvent(event: AddPlaceScreenEvent) {
        when (event) {
            AddPlaceScreenEvent.OnClickBack -> {
                viewModelScope.launch {
                    routeFlow.emit(AppRouter.RouteEvent.Back)
                }
            }
        }
    }
}