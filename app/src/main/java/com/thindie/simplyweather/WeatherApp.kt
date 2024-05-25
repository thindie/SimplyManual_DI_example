package com.thindie.simplyweather

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.thindie.simplyweather.presentation.add_place.screen.addPlaceScreen
import com.thindie.simplyweather.presentation.all_places.screen.allPlacesScreen
import com.thindie.simplyweather.presentation.detail_place.screen.detailPlaceScreen
import com.thindie.simplyweather.presentation.rename_place.screen.renamePlaceScreen
import com.thindie.simplyweather.routing.AppRouter

@Composable
fun WeatherApp(navHostController: NavHostController) {
    Surface(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navHostController,
            startDestination = AppRouter.RouteEvent.AllPlaces::class.java.name
        ) {
            detailPlaceScreen()
            allPlacesScreen()
            addPlaceScreen()
            renamePlaceScreen()
        }
    }
}