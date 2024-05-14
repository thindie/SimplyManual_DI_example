package com.thindie.simplyweather.presentation.add_place.viewmodel

import androidx.lifecycle.ViewModel
import com.thindie.simplyweather.domain.WeatherRepository
import com.thindie.simplyweather.routing.AppRouter
import kotlinx.coroutines.flow.MutableSharedFlow

class AddPlaceViewModel(
    private val repository: WeatherRepository,
    private val routeFlow: MutableSharedFlow<AppRouter.RouteEvent>,
) : ViewModel() {

}