package com.thindie.simplyweather.presentation.detail_place.viewmodel

import androidx.lifecycle.ViewModel
import com.thindie.simplyweather.domain.WeatherRepository
import com.thindie.simplyweather.routing.AppRouter
import kotlinx.coroutines.flow.MutableSharedFlow

class DetailPlaceViewModel(
    private val repository: WeatherRepository,
    private val routeFlow: MutableSharedFlow<AppRouter.RouteEvent>,
) : ViewModel() {

}