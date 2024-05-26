package com.thindie.simplyweather.presentation.stored_places.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
import com.thindie.simplyweather.presentation.stored_places.event.StoredPlacesEvent
import com.thindie.simplyweather.presentation.stored_places.state.StoredPlaceScreenState
import com.thindie.simplyweather.presentation.stored_places.viewmodel.StoredPlaceViewModel
import com.thindie.simplyweather.routing.AppRouter
import com.thindie.simplyweather.routing.OnBackPressedHandler

fun NavGraphBuilder.storedPlacesScreen() {
    composable(route = AppRouter.RouteEvent.StoredPlaces::class.java.name) {
        val dependenciesProvider =
            (LocalContext.current as DependenciesProvider.DependenciesHolder).getDependenciesProvider()
        val viewModel: StoredPlaceViewModel = viewModel(
            factory = dependenciesProvider.storedPlaceViewModelFactory
        )
        Screen(viewModel)
    }
}

@Composable
private fun Screen(viewModel: StoredPlaceViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle(
        initialValue = StoredPlaceScreenState(),
        minActiveState = Lifecycle.State.RESUMED
    )
    OnBackPressedHandler(onBack = { viewModel.onEvent(StoredPlacesEvent.OnBack) })
    LaunchedEffect(key1 = Unit) {
        viewModel.onEvent(StoredPlacesEvent.RequestStoredPlaces)
    }
    Box(modifier = Modifier.fillMaxSize()) {
        when (state.transitionState) {
            TransitionState.Content -> {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 24.dp)
                        .align(Alignment.TopStart),
                    text = "Сохранены места:",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .padding(horizontal = 48.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            text = "погоду можно будет узнать после соединения с интернет",
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Light,
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.LightGray
                        )
                    }
                    items(state.places) {
                        Divider(
                            modifier = Modifier.fillMaxWidth(),
                            thickness = Dp.Hairline
                        )
                        Text(
                            modifier = Modifier.padding(vertical = 24.dp),
                            text = it,
                            fontFamily = FontFamily.Default,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = Color.DarkGray
                            ),
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
                PrimaryButton(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(vertical = 24.dp),
                    title = "Назад",
                    leadingIcon = {},
                    onClick = { viewModel.onEvent(StoredPlacesEvent.OnBack) })
            }

            TransitionState.Error -> {

            }

            TransitionState.Loading -> {

            }

            TransitionState.None -> {

            }
        }

    }
}