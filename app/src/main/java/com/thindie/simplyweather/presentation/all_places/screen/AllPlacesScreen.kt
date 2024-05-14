package com.thindie.simplyweather.presentation.all_places.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.thindie.simplyweather.di.DependenciesProvider
import com.thindie.simplyweather.presentation.all_places.event.AllPlacesScreenEvent
import com.thindie.simplyweather.presentation.all_places.viewmodel.AllPlacesViewModel
import com.thindie.simplyweather.routing.AppRouter
import com.thindie.simplyweather.routing.OnBackPressedHandler

fun NavGraphBuilder.allPlacesScreen() {
    composable(
        route = AppRouter.RouteEvent.AllPlaces::class.java.name,
    ) {
        val provider =
            (LocalContext.current as DependenciesProvider.DependenciesHolder).getDependenciesProvider()
        val viewModel: AllPlacesViewModel = viewModel(
            factory = provider.allPlacesViewModelFactory
        )
        Screen(viewModel)
    }
}

@Composable
private fun Screen(viewModel: AllPlacesViewModel) {
    OnBackPressedHandler(onBack = { viewModel.onEvent(AllPlacesScreenEvent.OnClickBack) })
    Box {

        Button(onClick = { viewModel.onEvent(AllPlacesScreenEvent.OnClickBack) }) {
            Text(text = "AllPlacesScreen")
        }
    }
}