package com.thindie.simplyweather.presentation.all_places.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.thindie.simplyweather.di.DependenciesProvider
import com.thindie.simplyweather.presentation.PrimaryButton
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
        val dependenciesProvider =
            (LocalContext.current as DependenciesProvider.DependenciesHolder).getDependenciesProvider()
        val viewModel: AllPlacesViewModel = viewModel(
            factory = dependenciesProvider.allPlacesViewModelFactory
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
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (state.transitionState) {
            TransitionState.Content -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    item {
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 24.dp, vertical = 24.dp)
                                .align(Alignment.Start),
                            text = "Погода сейчас:",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                        )
                    }
                    val list = state.forecast.toList()
                    items(list) {
                        val precipitationTime =
                            state.expectedPrecipitationEvents
                                .firstOrNull { time ->
                                    time.latitude == it.second.latitude.toString()
                                            && time.longitude == it.second.latitude.toString()
                                }
                        CurrentWeather(
                            currentWeather = it.second,
                            currentWeatherTitle = it.first,
                            onClick = {
                                viewModel
                                    .onEvent(
                                        AllPlacesScreenEvent.RequestPlaceDetails(
                                            it.second
                                        )
                                    )
                            },
                            onExpect = {
                                viewModel.onEvent(
                                    AllPlacesScreenEvent.RequestPrecipitationTime(
                                        it.second
                                    )
                                )
                            },
                            expectedPrecipitationTime = precipitationTime?.time.orEmpty()
                        )
                        Divider(
                            modifier = Modifier
                                .padding(horizontal = 24.dp)
                                .fillMaxWidth(),
                            color = Color.Black,
                            thickness = Dp.Hairline
                        )
                    }


                    item {
                        PrimaryButton(
                            title = "Добавить место",
                            leadingIcon = {},
                            onClick = { viewModel.onEvent(AllPlacesScreenEvent.RequestAddPlaceScreen) }
                        )
                        HeightSpacer(dp = 24.dp)
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
                AllPlaceShimmer()
                LaunchedEffect(key1 = Unit) {
                    delay(1.seconds)
                    if (state.forecast.isEmpty()) {
                        viewModel.onEvent(
                            AllPlacesScreenEvent.EmptyForecast
                        )
                    }
                }
            }

            TransitionState.None -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 24.dp, vertical = 24.dp)
                            .align(Start),
                        text = "Пока ничего нет..",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                    )
                    Text(
                        text = "нужно добавить новое место, или проверить соединение с интернет",
                        modifier = Modifier.padding(horizontal = 48.dp),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Light,
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.LightGray
                    )
                    TextButton(
                        onClick = { viewModel.onEvent(AllPlacesScreenEvent.RequestStoredPlaces) },
                        contentPadding = PaddingValues(2.dp)
                    ) {
                        Text(
                            text = "посмотреть сохраненные места",
                            modifier = Modifier.padding(horizontal = 48.dp),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }

                    PrimaryButton(
                        modifier = Modifier.padding(all = 24.dp),
                        title = "Добавить",
                        leadingIcon = {},
                        onClick = { viewModel.onEvent(AllPlacesScreenEvent.RequestAddPlaceScreen) }
                    )
                }
            }
        }
    }
}
