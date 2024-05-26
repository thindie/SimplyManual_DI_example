package com.thindie.simplyweather.presentation.add_place.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.thindie.simplyweather.di.DependenciesProvider
import com.thindie.simplyweather.presentation.PrimaryButton
import com.thindie.simplyweather.presentation.SimpleSnackBar
import com.thindie.simplyweather.presentation.TransitionState
import com.thindie.simplyweather.presentation.add_place.event.AddPlaceScreenEvent
import com.thindie.simplyweather.presentation.add_place.viewmodel.AddPlaceViewModel
import com.thindie.simplyweather.presentation.add_place.viewstate.AddPlaceError
import com.thindie.simplyweather.presentation.add_place.viewstate.AddPlaceState
import com.thindie.simplyweather.presentation.add_place.viewstate.AddPlaceSuccess
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun Screen(viewModel: AddPlaceViewModel) {
    OnBackPressedHandler(onBack = { viewModel.onEvent(AddPlaceScreenEvent.OnClickBack) })
    LaunchedEffect(key1 = Unit) {
        viewModel.onEvent(AddPlaceScreenEvent.EnterScreen)
    }
    val state by
    viewModel.state
        .collectAsStateWithLifecycle(
            initialValue = AddPlaceState(),
            minActiveState = Lifecycle.State.RESUMED
        )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .navigationBarsPadding()
            .padding(bottom = 24.dp)
    )
    {
        if (state.addPlaceError != null || state.addPlaceSuccess != null) {
            SimpleSnackBar(
                modifier = Modifier.zIndex(2f),
                isSuccess = state.addPlaceError == null,
                message = getSnackBarMessage(state)
            ) {
                viewModel.onEvent(AddPlaceScreenEvent.DismissSnack)
            }
        }
        when (state.transitionState) {
            TransitionState.None -> {
                AddPlaceShimmer()
            }

            TransitionState.Error -> {}
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                           ,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        HeightSpacer(dp = 24.dp)
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 24.dp)
                                .align(Start),
                            text = "Добавить место",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                        )
                        IconButton(
                            modifier = Modifier
                                .align(End)
                                .padding(all = 24.dp),
                            onClick = { viewModel.onEvent(AddPlaceScreenEvent.RequestAllPlacesScreen) },
                            content = {
                                Icon(
                                    modifier = Modifier
                                        .size(24.dp),
                                    painter = rememberVectorPainter(image = Icons.Default.ArrowForward),
                                    contentDescription = "forward"
                                )
                            }
                        )
                        HeightSpacer(dp = 24.dp)
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        HeightSpacer(dp = 48.dp)
                        AppOutlinedTextField(
                            value = state.placeTitle,
                            onValueChange = {
                                viewModel.onEvent(
                                    AddPlaceScreenEvent.PlaceNameUpdate(it)
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            isError = state.titleError != null,
                            transitionState = state.transitionState,
                            onClickTrailingIcon = {
                                viewModel.onEvent(
                                    AddPlaceScreenEvent.PlaceNameUpdate("")
                                )
                            },
                            onError = {
                                Text(
                                    fontWeight = FontWeight.Light,
                                    fontFamily = FontFamily.Monospace,
                                    style = MaterialTheme.typography.labelSmall,
                                    text = "нужно указать название места"
                                )
                            },
                            placeHolder = {
                                Text(
                                    text = "Место..",
                                    fontStyle = FontStyle.Italic,
                                    fontWeight = FontWeight.Light,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        )
                        HeightSpacer(dp = 8.dp)
                        TextButton(
                            enabled = state.placeTitle.length > 4 && state.placeTitle.isNotBlank(),
                            onClick = {
                                viewModel.onEvent(
                                    AddPlaceScreenEvent.RequestPlaceDetection(state.placeTitle)
                                )
                            },
                        ) {
                            Text("Сыскать по названию")
                        }

                        HeightSpacer(dp = 8.dp)
                        AppOutlinedTextField(
                            value = state.latitude,
                            onValueChange = {
                                viewModel.onEvent(
                                    AddPlaceScreenEvent.LatitudeUpdate(it)
                                )
                            },

                            isError = state.latitudeError != null,
                            transitionState = state.transitionState,
                            onClickTrailingIcon = {
                                viewModel.onEvent(
                                    AddPlaceScreenEvent.LatitudeUpdate("")
                                )
                            },
                            onError = {
                                Text(
                                    fontWeight = FontWeight.Light,
                                    fontFamily = FontFamily.Monospace,
                                    style = MaterialTheme.typography.labelSmall,
                                    text = "широта должна быть заполнена"
                                )
                            },
                            placeHolder = {
                                Text(
                                    text = "широта..",
                                    fontStyle = FontStyle.Italic,
                                    fontWeight = FontWeight.Light,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        )
                        HeightSpacer(dp = 8.dp)
                        AppOutlinedTextField(
                            value = state.longitude,
                            onValueChange = {
                                viewModel.onEvent(
                                    AddPlaceScreenEvent.LongitudeUpdate(it)
                                )
                            },
                            isError = state.longitudeError != null,
                            transitionState = state.transitionState,
                            onClickTrailingIcon = {
                                viewModel.onEvent(
                                    AddPlaceScreenEvent.LongitudeUpdate("")
                                )
                            },
                            onError = {
                                Text(
                                    fontWeight = FontWeight.Light,
                                    fontFamily = FontFamily.Monospace,
                                    style = MaterialTheme.typography.labelSmall,
                                    text = "долгота должна быть заполнена"
                                )
                            },
                            placeHolder = {
                                Text(
                                    text = "долгота..",
                                    fontStyle = FontStyle.Italic,
                                    fontWeight = FontWeight.Light,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        )
                    }
                    PrimaryButton(
                        onClick = { viewModel.onEvent(AddPlaceScreenEvent.AddPlaceApply) },
                        enabled = state.transitionState == TransitionState.Content,
                        leadingIcon = {
                            LoadingLeadingIcon(
                                state = state.transitionState
                            )
                        },
                        title = "Принять"
                    )
                    HeightSpacer(dp = 16.dp)
                }
            }
        }
        when (state.detectionPlaceTransitionState) {
            TransitionState.None -> {}
            TransitionState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .align(Alignment.TopCenter),
                    )
                }
            }

            else -> ModalBottomSheet(
                onDismissRequest = { viewModel.onEvent(AddPlaceScreenEvent.DismissPlaceDetection) }
            ) {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    when (state.detectionPlaceTransitionState) {
                        TransitionState.Content -> {
                            stickyHeader {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(72.dp)
                                        .clip(MaterialTheme.shapes.large),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Место",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "Координаты",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Light
                                    )
                                }
                            }
                            items(
                                items = state.weatherPlacePossibility,
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(72.dp)
                                        .clip(MaterialTheme.shapes.large)
                                        .clickable {
                                            viewModel.onEvent(
                                                AddPlaceScreenEvent.ApplyPlaceDetection(
                                                    it
                                                )
                                            )
                                        },
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        modifier = Modifier,
                                        text = it.displayName.take(15),
                                        maxLines = 1,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                    Text(
                                        modifier = Modifier,
                                        text = it.latitude.take(5),
                                        maxLines = 1,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Thin,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                    Text(
                                        modifier = Modifier,
                                        text = it.longitude.take(5),
                                        maxLines = 1,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Thin,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }
                                Divider(
                                    modifier = Modifier.fillMaxWidth(0.7f),
                                    thickness = Dp.Hairline
                                )
                            }
                            item {
                                HeightSpacer(dp = 72.dp)
                            }
                        }

                        TransitionState.Error -> {
                            item {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    HeightSpacer(dp = 72.dp)
                                    Text(
                                        text = "Похоже, ничего не нашлось",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.headlineSmall
                                    )
                                    HeightSpacer(dp = 8.dp)
                                    Text(
                                        text = "Нужно уточнить запрос",
                                        fontWeight = FontWeight.Medium,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    HeightSpacer(dp = 72.dp)
                                }

                            }
                        }

                        else -> {}
                    }
                    item {
                        HeightSpacer(dp = 72.dp)
                    }
                }
            }
        }
    }
}

fun getSnackBarMessage(state: AddPlaceState): String? {
    var message: String? = null

    when (state.addPlaceError) {
        AddPlaceError.EmptyAutoFind -> {}
        AddPlaceError.LatitudeValidation -> {}
        AddPlaceError.LongitudeValidation -> {}
        AddPlaceError.PlaceValidation -> {}
        AddPlaceError.RequestAddPlaceUnSuccess -> {
            message = "Не смогли добавить новое место"
        }

        AddPlaceError.RequestAutoFindUnSuccess -> {
            message = "Не получилось найти место"
        }

        null -> {}
    }

    when (state.addPlaceSuccess) {
        AddPlaceSuccess.SuccessAddition -> {
            message = "Место успешно добавлено"
        }

        null -> {}
    }

    return message
}


@Composable
fun HeightSpacer(dp: Dp) {
    Spacer(modifier = Modifier.height(dp))
}

@Suppress("LongParameterList")
@Composable
fun AppOutlinedTextField(
    transitionState: TransitionState,
    value: String,
    onValueChange: (String) -> Unit,
    onClickTrailingIcon: () -> Unit,
    isError: Boolean,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    placeHolder: @Composable () -> Unit,
    onError: @Composable () -> Unit,
) {
    OutlinedTextField(
        value = value,
        keyboardOptions = keyboardOptions,
        onValueChange = onValueChange,
        leadingIcon = {
            LoadingLeadingIcon(state = transitionState)
        },
        isError = isError,
        trailingIcon = {
            AnimatedVisibility(visible = value.isNotBlank()) {
                DismissIcon(
                    onClick = onClickTrailingIcon
                )
            }
        },
        supportingText = {
            if (isError) {
                onError()
            }
        },
        placeholder = { placeHolder() },
        maxLines = 1,
        singleLine = true,
        shape = MaterialTheme.shapes.large
    )
}

@Composable
private fun LoadingLeadingIcon(state: TransitionState) {
    if (state == TransitionState.Loading) {
        CircularProgressIndicator(
            modifier = Modifier.size(12.dp),
            strokeWidth = 1.5.dp
        )
    }
}

@Composable
private fun DismissIcon(
    onClick: () -> Unit,
) {
    IconButton(onClick = onClick) {
        Icon(
            modifier = Modifier
                .size(12.dp),
            painter = rememberVectorPainter(image = Icons.Default.Clear),
            contentDescription = "clear"
        )
    }
}
