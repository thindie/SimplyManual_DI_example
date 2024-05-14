package com.thindie.simplyweather.routing

import kotlinx.coroutines.flow.MutableSharedFlow

class AppRouter {
    private val _routeEvent: MutableSharedFlow<RouteEvent> = MutableSharedFlow()
    val routeFlow
        get() = _routeEvent

    sealed class RouteEvent {
        data object Back: RouteEvent()
        data class DetailPlace(val latitude: String, val longitude: String): RouteEvent()
        data object AllPlaces: RouteEvent()
        data object AddPlace: RouteEvent()
    }
}