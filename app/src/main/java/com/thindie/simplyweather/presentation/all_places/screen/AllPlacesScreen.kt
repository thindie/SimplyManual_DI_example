package com.thindie.simplyweather.presentation.all_places.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.thindie.simplyweather.di.DependenciesProvider
import com.thindie.simplyweather.presentation.TransitionState
import com.thindie.simplyweather.presentation.all_places.event.AllPlacesScreenEvent
import com.thindie.simplyweather.presentation.all_places.viewmodel.AllPlacesViewModel
import com.thindie.simplyweather.presentation.all_places.viewstate.AllPlacesState
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

    LaunchedEffect(key1 = Unit) {
        viewModel.onEvent(AllPlacesScreenEvent.EnteringScreen)
    }

    val state by viewModel
        .screenState
        .collectAsStateWithLifecycle(
            minActiveState = Lifecycle.State.RESUMED,
            initialValue = AllPlacesState()
        )
    Box {
        when (state.transitionState){
            TransitionState.Content -> {
                Button(onClick = { viewModel.onEvent(AllPlacesScreenEvent.OnClickBack) }) {
                    Text(text = state.forecast?.apparentTempMax?.firstOrNull().toString())
                }
            }
            TransitionState.Error -> {
                Spacer(modifier = Modifier
                    .size(36.dp)
                    .background(color = Color.Red))
            }
            TransitionState.Loading -> {
                LinearProgressIndicator()
            }
            TransitionState.None -> {
                CircularProgressIndicator()
            }
        }

    }
}