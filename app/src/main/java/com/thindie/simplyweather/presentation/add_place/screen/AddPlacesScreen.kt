package com.thindie.simplyweather.presentation.add_place.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.thindie.simplyweather.di.DependenciesProvider
import com.thindie.simplyweather.presentation.add_place.event.AddPlaceScreenEvent
import com.thindie.simplyweather.presentation.add_place.viewmodel.AddPlaceViewModel
import com.thindie.simplyweather.routing.AppRouter
import com.thindie.simplyweather.routing.OnBackPressedHandler

fun NavGraphBuilder.addPlaceScreen() {
    composable(
        route = AppRouter.RouteEvent.AddPlace::class.java.name,
    ) {
        val provider =
            (LocalContext.current as DependenciesProvider.DependenciesHolder).getDependenciesProvider()
        val viewModel: AddPlaceViewModel = viewModel(
            factory = provider.addPlaceViewModelFactory
        )
        Screen(viewModel)
    }
}

@Composable
private fun Screen(viewModel: AddPlaceViewModel) {
    OnBackPressedHandler(onBack = { viewModel.onEvent(AddPlaceScreenEvent.OnClickBack) })
    Box {

        Button(onClick = { viewModel.onEvent(AddPlaceScreenEvent.OnClickBack) }) {
            Text(text = "AddPlaceScreen")
        }
    }
}