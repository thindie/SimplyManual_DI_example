package com.thindie.simplyweather.presentation.detail_place.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.thindie.simplyweather.di.DependenciesProvider
import com.thindie.simplyweather.presentation.detail_place.event.DetailPlaceScreenEvent
import com.thindie.simplyweather.presentation.detail_place.viewmodel.DetailPlaceViewModel
import com.thindie.simplyweather.routing.AppRouter
import com.thindie.simplyweather.routing.OnBackPressedHandler

fun NavGraphBuilder.detailPlaceScreen() {
    composable(
        route = AppRouter.RouteEvent.DetailPlace::class.java.name.plus("/{latitude###longitude}"),
        arguments = listOf(navArgument("latitude###longitude") { type = NavType.StringType })
    ) {
        val arguments = it.arguments?.getString("latitude###longitude")
        val args = arguments?.removePrefix("{latitude###longitude}")
        val latitude = args?.substringBefore("###")
        val longitude = args?.substringAfter("###")

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

}
