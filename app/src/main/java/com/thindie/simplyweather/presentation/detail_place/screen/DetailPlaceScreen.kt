package com.thindie.simplyweather.presentation.detail_place.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.thindie.simplyweather.di.DependenciesProvider
import com.thindie.simplyweather.domain.DailyForecast
import com.thindie.simplyweather.domain.HourlyForecast
import com.thindie.simplyweather.presentation.TransitionState
import com.thindie.simplyweather.presentation.add_place.screen.HeightSpacer
import com.thindie.simplyweather.presentation.detail_place.event.DetailPlaceScreenEvent
import com.thindie.simplyweather.presentation.detail_place.viewmodel.DetailPlaceViewModel
import com.thindie.simplyweather.presentation.detail_place.viewstate.DetailScreenState
import com.thindie.simplyweather.presentation.getTemporalAccessor
import com.thindie.simplyweather.presentation.toUiString
import com.thindie.simplyweather.presentation.weatherIcon
import com.thindie.simplyweather.routing.AppRouter
import com.thindie.simplyweather.routing.OnBackPressedHandler
import okhttp3.internal.format

fun NavGraphBuilder.detailPlaceScreen() {
    composable(
        route = AppRouter.RouteEvent.DetailPlace::class.java.name.plus("/{coordinates}"),
        arguments = listOf(navArgument("coordinates") { type = NavType.StringType })
    ) {
        val arguments = it.arguments?.getString("coordinates")
        val args = arguments?.removePrefix("{coordinates-")
        val latitude = args?.substringBefore("_")
        val longitude = args?.substringAfter("_")?.removeSuffix("}")
        val dependenciesProvider =
            (LocalContext.current as DependenciesProvider.DependenciesHolder).getDependenciesProvider()
        val viewModel: DetailPlaceViewModel = viewModel(
            factory = dependenciesProvider.getDetailScreenViewModelFactory(
                latitude.orEmpty(), longitude.orEmpty()
            )
        )
        Screen(viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Screen(viewModel: DetailPlaceViewModel) {
    OnBackPressedHandler(onBack = { viewModel.onEvent(DetailPlaceScreenEvent.OnBackClick) })
    LaunchedEffect(key1 = Unit) {
        viewModel.onEvent(DetailPlaceScreenEvent.RequestDetailForecast)
    }
    val state by viewModel.state.collectAsStateWithLifecycle(
        initialValue = DetailScreenState(), minActiveState = Lifecycle.State.RESUMED
    )
    Box(modifier = Modifier.fillMaxSize()) {
        when (state.transitionState) {
            TransitionState.Content -> {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(modifier = Modifier) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 24.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier.size(72.dp),
                                painter = painterResource(id = weatherIcon(state.weeklyForecast[0].weatherCode)),
                                contentDescription = null,
                                tint = LocalContentColor.current.copy(0.7f)
                            )
                            Column  {
                                val tcMax = state.weeklyForecast[0].apparentTempMax
                                val tcMin = state.weeklyForecast[0].apparentTempMin
                                Text(
                                    text = tcMax.toString(),
                                    modifier = Modifier,
                                    fontWeight = FontWeight.Black,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        color = if (tcMax > 0) Color.Red else Color.Blue.copy(0.7f)
                                    ),
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = tcMin.toString(),
                                    modifier = Modifier,
                                    fontWeight = FontWeight.Light,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        color = if (tcMin > 0) Color.Red else Color.Blue.copy(0.7f)
                                    ),
                                    fontSize = 24.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = state.title.ifBlank {
                                    format(
                                        "Неизвестное место, %s Ш  %s Д",
                                        state.weeklyForecast[0].latitude.toString(),
                                        state.weeklyForecast[0].longitude.toString(),
                                    )
                                },
                                modifier = Modifier,
                                maxLines = 1,
                                fontWeight = FontWeight.Bold,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.headlineLarge
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 24.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = format(
                                    "ветер: %s м/с",
                                    state.weeklyForecast[0].windSpeed10mMax.toString()
                                ),
                                modifier = Modifier,
                                fontWeight = FontWeight.Light,
                                color = LocalTextStyle.current.color.copy(0.5f),
                                style = MaterialTheme.typography.labelLarge,
                            )
                            Text(
                                text = format(
                                    "осадки %s мм",
                                    state.weeklyForecast[0].precipitationSum.toString()
                                ),
                                fontWeight = FontWeight.Light,
                                color = LocalTextStyle.current.color.copy(0.5f),
                                style = MaterialTheme.typography.labelLarge,
                            )
                        }
                    }
                    Divider(
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .fillMaxWidth(),
                        thickness = 2.dp
                    )
                    HeightSpacer(dp = 8.dp)
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp),
                    ) {
                        items(state.weeklyForecast, key = DailyForecast::hashCode) {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .clip(MaterialTheme.shapes.medium)
                                .clickable {
                                    viewModel.onEvent(
                                        DetailPlaceScreenEvent.RequestHourlyForecast(it)
                                    )
                                }
                                .height(72.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly) {
                                val temporalAccessor = getTemporalAccessor(it.sunrise)
                                // date
                                Row(
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    Text(
                                        text = temporalAccessor.toUiString("d"),
                                        fontWeight = FontWeight.ExtraBold,
                                        style = MaterialTheme.typography.bodyLarge,
                                    )
                                    Text(
                                        text = temporalAccessor.toUiString("EEE"),
                                        fontWeight = FontWeight.Light,
                                        style = MaterialTheme.typography.bodyLarge,
                                    )
                                }
                                // temperature
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = it.apparentTempMax.toString(),
                                        fontWeight = FontWeight.Black,
                                        style = MaterialTheme.typography.titleLarge,
                                        color = if (it.apparentTempMax > 0.0) Color.Red else Color.Blue
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = format("%s C", it.apparentTempMin.toString()),
                                        fontWeight = FontWeight.Normal,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (it.apparentTempMin > 0.0) Color.Red else Color.Blue
                                    )
                                }
                                //precipitation
                                Row(
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    Text(
                                        text = it.precipitationSum.toString(),
                                        fontWeight = FontWeight.Black,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = "mm",
                                        fontWeight = FontWeight.Thin,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                                // windspeed
                                Row(
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    Text(
                                        text = it.windSpeed10mMax.toString(),
                                        fontWeight = FontWeight.Black,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = "m/s",
                                        fontWeight = FontWeight.Thin,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Divider(
                                modifier = Modifier.fillMaxWidth(), thickness = Dp.Hairline
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                        item {
                            HeightSpacer(dp = 24.dp)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ClickableText(text = AnnotatedString("Вернуться"),
                                    modifier = Modifier,

                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Light,
                                    ),
                                    onClick = {
                                        viewModel.onEvent(
                                            DetailPlaceScreenEvent.OnBackClick
                                        )
                                    })
                                Spacer(modifier = Modifier.width(24.dp))
                                ClickableText(text = AnnotatedString("Действия"),
                                    modifier = Modifier,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 18.sp,
                                    ),
                                    onClick = {
                                        viewModel.onEvent(
                                            DetailPlaceScreenEvent.TriggerDropDownMenu
                                        )
                                    })
                                DropdownMenu(expanded = state.isDropDownResumed,
                                    onDismissRequest = {
                                        viewModel.onEvent(
                                            DetailPlaceScreenEvent.TriggerDropDownMenu
                                        )
                                    }) {
                                    DropdownMenuItem(text = {
                                        Text(
                                            text = "Переименовать",
                                            modifier = Modifier,
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 16.sp,
                                            ),
                                        )
                                    }, onClick = {
                                        viewModel.onEvent(
                                            DetailPlaceScreenEvent.TriggerChangeTitle
                                        )
                                    })
                                    Divider(
                                        modifier = Modifier.fillMaxWidth(), thickness = Dp.Hairline
                                    )
                                    DropdownMenuItem(text = {
                                        Text(
                                            text = "Удалить",
                                            modifier = Modifier,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontWeight = FontWeight.Light,
                                            ),
                                        )
                                    }, onClick = {
                                        viewModel.onEvent(
                                            DetailPlaceScreenEvent.DeletePlace
                                        )
                                    })
                                }
                            }
                            HeightSpacer(dp = 24.dp)
                        }
                    }
                }
                if (state.isHourlyWeatherRequested) {
                    ModalBottomSheet(onDismissRequest = {
                        viewModel.onEvent(DetailPlaceScreenEvent.DismissHourlyForecast)
                    }
                    ) {
                        when (state.hourlyTransitionState) {
                            TransitionState.Content -> {
                                HeightSpacer(dp = 8.dp)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 24.dp)
                                        .align(Alignment.CenterHorizontally),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "часы",
                                        fontWeight = FontWeight.Medium,
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "температура*",
                                            fontWeight = FontWeight.Thin,
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                        Text(
                                            text = "(комфорт)",
                                            fontWeight = FontWeight.Thin,
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }

                                    Text(
                                        text = "осадки",
                                        fontWeight = FontWeight.Black,
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                    Text(
                                        text = "ветер",
                                        fontWeight = FontWeight.Medium,
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                                HeightSpacer(dp = 12.dp)
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    items(state.hourlyList, key = HourlyForecast::hashCode) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(MaterialTheme.shapes.medium)
                                                .height(72.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceEvenly
                                        ) {
                                            val temporalAccessor = getTemporalAccessor(it.time)
                                            // date
                                            Row(
                                                verticalAlignment = Alignment.Bottom
                                            ) {
                                                Text(
                                                    text = temporalAccessor.toUiString("HH"),
                                                    fontWeight = FontWeight.SemiBold,
                                                    style = MaterialTheme.typography.titleLarge,
                                                )
                                                Text(
                                                    text = temporalAccessor.toUiString(":mm"),
                                                    fontWeight = FontWeight.Light,
                                                    style = MaterialTheme.typography.bodyLarge,
                                                )
                                            }
                                            // temperature
                                            Text(
                                                text = it.temperature2m.toString(),
                                                fontWeight = FontWeight.Black,
                                                style = MaterialTheme.typography.titleLarge,
                                                color = if (it.temperature2m > 0.0) Color.Red else Color.Blue
                                            )
                                            //precipitation
                                            Row(
                                                verticalAlignment = Alignment.Bottom
                                            ) {
                                                Text(
                                                    text = it.precipitation.toString(),
                                                    fontWeight = FontWeight.Black,
                                                    style = MaterialTheme.typography.bodyLarge
                                                )
                                                Text(
                                                    text = "mm",
                                                    fontWeight = FontWeight.Thin,
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                            }
                                            // windspeed
                                            Row(
                                                verticalAlignment = Alignment.Bottom
                                            ) {
                                                Text(
                                                    text = it.windSpeed10m.toString(),
                                                    fontWeight = FontWeight.Black,
                                                    style = MaterialTheme.typography.bodyLarge
                                                )
                                                Text(
                                                    text = "m/s",
                                                    fontWeight = FontWeight.Thin,
                                                    style = MaterialTheme.typography.bodyLarge
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Divider(
                                            modifier = Modifier.fillMaxWidth(),
                                            thickness = Dp.Hairline
                                        )
                                    }
                                }
                            }

                            TransitionState.Error -> {}
                            TransitionState.Loading -> DetailPlaceShimmer()
                            TransitionState.None -> DetailPlaceShimmer()
                        }
                    }
                }
            }

            TransitionState.Error -> {

            }

            TransitionState.Loading -> {
                DetailPlaceShimmer()
            }

            TransitionState.None -> {
                DetailPlaceShimmer()
            }
        }
    }
}
