package com.thindie.simplyweather.routing

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.thindie.simplyweather.di.DependenciesProvider
import kotlinx.coroutines.flow.MutableSharedFlow

class AppRouter private constructor(){
    private val _routeEvent: MutableSharedFlow<RouteEvent> = MutableSharedFlow()
    val routeFlow
        get() = _routeEvent

    sealed class RouteEvent {
        data object Back : RouteEvent()
        data class DetailPlace(val latitude: String, val longitude: String) : RouteEvent()
        data object AllPlaces : RouteEvent()
        data object AddPlace : RouteEvent()
    }
    companion object {
        fun inject(dependenciesProvider: DependenciesProvider){
            dependenciesProvider.setAppRouter(AppRouter())
        }
    }
}

private fun NavHostController.forward(route: String) {
    navigate(route) {
        launchSingleTop = true
        restoreState = true
    }
}

fun NavHostController.onRouteEvent(routeEvent: AppRouter.RouteEvent) {
    when (routeEvent) {
        AppRouter.RouteEvent.AddPlace -> forward(AppRouter.RouteEvent.AddPlace::class.java.name)
        AppRouter.RouteEvent.AllPlaces -> forward(AppRouter.RouteEvent.AllPlaces::class.java.name)
        AppRouter.RouteEvent.Back -> popBackStack()
        is AppRouter.RouteEvent.DetailPlace -> {
            val latitude = routeEvent.latitude
            val longitude = routeEvent.longitude
            forward(AppRouter.RouteEvent.DetailPlace::class.java.name.plus("/{$latitude###$longitude}"))
        }
    }
}

@Composable
fun OnBackPressedHandler(
    onBack: () -> Unit,
    enabled: Boolean = true,
) {
    BackHandler(enabled, onBack)
}