package com.thindie.simplyweather.presentation.detail_place.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.thindie.simplyweather.di.DependenciesProvider
import com.thindie.simplyweather.presentation.detail_place.event.DetailPlaceScreenEvent
import com.thindie.simplyweather.presentation.detail_place.viewmodel.DetailPlaceViewModel
import com.thindie.simplyweather.presentation.detail_place.viewstate.DetailScreenState
import com.thindie.simplyweather.routing.AppRouter
import com.thindie.simplyweather.routing.OnBackPressedHandler

fun NavGraphBuilder.detailPlaceScreen() {
    composable(
        route = AppRouter.RouteEvent.DetailPlace::class.java.name.plus("/{coordinates}"),
        arguments = listOf(navArgument("coordinates") { type = NavType.StringType })
    ) {
        val arguments = it.arguments?.getString("coordinates")
        val args = arguments?.removePrefix("{coordinates-")
        val latitude = args?.substringBefore("_")
        val longitude = args?.substringAfter("_")?.removeSuffix("}")
        val provider =
            (LocalContext.current as DependenciesProvider.DependenciesHolder).getDependenciesProvider()
        val viewModel: DetailPlaceViewModel = viewModel(
            factory = provider.getDetailScreenViewModelFactory(
                latitude.orEmpty(),
                longitude.orEmpty()
            )
        )
        Screen(viewModel)
    }
}

@Composable
private fun Screen(viewModel: DetailPlaceViewModel) {
    OnBackPressedHandler(onBack = { viewModel.onEvent(DetailPlaceScreenEvent.OnBackClick) })
    LaunchedEffect(key1 = Unit) {
        viewModel.onEvent(DetailPlaceScreenEvent.RequestDetailForecast)
    }
    val state = viewModel.state.collectAsStateWithLifecycle(
        initialValue = DetailScreenState(),
        minActiveState = Lifecycle.State.RESUMED
    )

    Text(
        text = state.value.weeklyForecast.size.toString()
    )
}
