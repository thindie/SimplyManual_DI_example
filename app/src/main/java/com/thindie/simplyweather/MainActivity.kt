package com.thindie.simplyweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.thindie.simplyweather.di.DependenciesProvider
import com.thindie.simplyweather.routing.AppRouter
import com.thindie.simplyweather.routing.onRouteEvent
import com.thindie.simplyweather.ui.theme.SimplyWeatherTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : ComponentActivity(), DependenciesProvider.DependenciesHolder {

    private lateinit var navController: NavHostController
    lateinit var appRouter: AppRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dependenciesHolder = application as DependenciesProvider.DependenciesHolder
        dependenciesHolder.getDependenciesProvider().inject(this)

        setContent {
            SimplyWeatherTheme {
                navController = rememberNavController()
                ObserveNavigation()
                WeatherApp(
                    navHostController = navController,
                )
            }
        }
    }

    @Composable
    fun ObserveNavigation() {
        LaunchedEffect(navController) {
            appRouter
                .routeFlow
                .onEach(navController::onRouteEvent)
                .launchIn(this)
        }
    }

    override fun setDependenciesProvider(dependenciesProvider: DependenciesProvider) {
        /*ignore*/
    }

    override fun getDependenciesProvider(): DependenciesProvider {
        return (application as DependenciesProvider.DependenciesHolder).getDependenciesProvider()
    }
}
