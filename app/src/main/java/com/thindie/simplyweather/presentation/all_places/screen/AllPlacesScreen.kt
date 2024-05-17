package com.thindie.simplyweather.presentation.all_places.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.thindie.simplyweather.di.DependenciesProvider
import com.thindie.simplyweather.presentation.TransitionState
import com.thindie.simplyweather.presentation.add_place.screen.HeightSpacer
import com.thindie.simplyweather.presentation.all_places.event.AllPlacesScreenEvent
import com.thindie.simplyweather.presentation.all_places.viewmodel.AllPlacesViewModel
import com.thindie.simplyweather.presentation.all_places.viewstate.AllPlacesState
import com.thindie.simplyweather.routing.AppRouter
import com.thindie.simplyweather.routing.OnBackPressedHandler
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

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
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (state.transitionState) {
            TransitionState.Content -> {
                IconButton(
                    modifier = Modifier.align(Alignment.Start),
                    onClick = { viewModel.onEvent(AllPlacesScreenEvent.OnClickBack) }
                ) {
                    Icon(
                        painter = rememberVectorPainter(image = Icons.Default.ArrowBack),
                        contentDescription = "backClick"
                    )
                }
                HeightSpacer(dp = 8.dp)
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(state.forecast) {
                        CurrentWeather(
                            currentWeather = it,
                            onClick = {

                            }
                        )
                    }
                }
            }

            TransitionState.Error -> {
                Spacer(
                    modifier = Modifier
                        .size(36.dp)
                        .background(color = Color.Red)
                )
            }

            TransitionState.Loading -> {
                CircularProgressIndicator(
                    strokeWidth = 1.5.dp
                )
                LaunchedEffect(key1 = Unit) {
                    delay(2.seconds)
                    if (state.forecast.isEmpty()) {
                        viewModel.onEvent(
                            AllPlacesScreenEvent.EmptyForecast
                        )
                    }
                }
            }

            TransitionState.None -> {
                Surface(
                    modifier = Modifier
                        .height(72.dp)
                        .fillMaxWidth(0.9f),
                    shadowElevation = 8.dp,
                    shape = MaterialTheme.shapes.medium,
                    onClick = {
                        viewModel.onEvent(AllPlacesScreenEvent.RequestAddPlaceScreen)
                    }
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Nothing to show here..",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Black
                        )
                        HeightSpacer(dp = 4.dp)
                        Text(
                            text = "add",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Thin
                        )
                    }
                }
            }
        }
    }
}
