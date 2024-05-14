package com.thindie.simplyweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import com.thindie.simplyweather.di.DependenciesProvider
import com.thindie.simplyweather.routing.AppRouter
import com.thindie.simplyweather.ui.theme.SimplyWeatherTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    lateinit var appRouter: AppRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dependenciesHolder = application as DependenciesProvider.DependenciesHolder
        dependenciesHolder.getDependenciesProvider().inject(this)

        setContent {
            SimplyWeatherTheme {
                ObserveNavigation()
            }
        }
    }

    @Composable
    fun ObserveNavigation() {
        LaunchedEffect(navController) {
            appRouter
                .routeFlow
                .onEach {
                    when(it){
                        AppRouter.RouteEvent.AddPlace -> TODO()
                        AppRouter.RouteEvent.AllPlaces -> TODO()
                        AppRouter.RouteEvent.Back -> TODO()
                        is AppRouter.RouteEvent.DetailPlace -> TODO()
                    }
                }
                .launchIn(this)
        }
    }
}
