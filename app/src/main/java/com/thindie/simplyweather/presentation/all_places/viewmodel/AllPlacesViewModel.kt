package com.thindie.simplyweather.presentation.all_places.viewmodel

import androidx.lifecycle.ViewModel
import com.thindie.simplyweather.domain.WeatherRepository
import com.thindie.simplyweather.routing.AppRouter
import kotlinx.coroutines.flow.MutableSharedFlow

class AllPlacesViewModel(
    private val repository: WeatherRepository,
    private val routeFlow: MutableSharedFlow<AppRouter.RouteEvent>,
) : ViewModel() {

}