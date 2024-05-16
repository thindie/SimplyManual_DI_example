package com.thindie.simplyweather.presentation.add_place.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.thindie.simplyweather.di.DependenciesProvider
import com.thindie.simplyweather.presentation.TransitionState
import com.thindie.simplyweather.presentation.add_place.event.AddPlaceScreenEvent
import com.thindie.simplyweather.presentation.add_place.viewmodel.AddPlaceViewModel
import com.thindie.simplyweather.presentation.add_place.viewstate.AddPlaceState
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
    LaunchedEffect(key1 = Unit) {
        viewModel.onEvent(AddPlaceScreenEvent.EnterScreen)
    }
    val state by
    viewModel.state
        .collectAsStateWithLifecycle(
            initialValue = AddPlaceState(),
            minActiveState = Lifecycle.State.RESUMED
        )
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (state.transitionState) {
            TransitionState.None -> {
                CircularProgressIndicator(strokeWidth = 1.5.dp)
            }

            else -> {
                IconButton(
                    modifier = Modifier.align(Alignment.Start),
                    onClick = { viewModel.onEvent(AddPlaceScreenEvent.OnClickBack) }
                ) {
                    Icon(
                        painter = rememberVectorPainter(Icons.Default.ArrowBack),
                        contentDescription = "backArrow"
                    )
                }
                HeightSpacer(dp = 32.dp)
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(64.dp),
                    shadowElevation = 8.dp,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Add new location",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Black
                        )
                    }

                }
                HeightSpacer(dp = 48.dp)
                AddPlaceOutlinedTextField(
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
                        Text(text = "title cannot be blank")
                    },
                    placeHolder = {
                        Text(
                            text = "location..",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                )
                HeightSpacer(dp = 8.dp)
                AddPlaceOutlinedTextField(
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
                        Text(text = "latitude must be digit-like")
                    },
                    placeHolder = {
                        Text(
                            text = "location latitude..",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                )
                HeightSpacer(dp = 8.dp)
                AddPlaceOutlinedTextField(
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
                        Text(text = "longitude must be digit-like")
                    },
                    placeHolder = {
                        Text(
                            text = "location longitude..",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                )
                HeightSpacer(dp = 72.dp)
                ElevatedButton(
                    modifier = Modifier
                        .height(72.dp)
                        .fillMaxWidth(0.8f),
                    onClick = { viewModel.onEvent(AddPlaceScreenEvent.AddPlaceApply) },
                    enabled = state.transitionState == TransitionState.Content,
                    shape = MaterialTheme.shapes.medium
                ) {
                    LoadingLeadingIcon(state = state.transitionState)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Apply")
                }
            }
        }
    }
}

@Composable
fun HeightSpacer(dp: Dp) {
    Spacer(modifier = Modifier.height(dp))
}
@Suppress("LongParameterList")
@Composable
private fun AddPlaceOutlinedTextField(
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
            painter = rememberVectorPainter(image = Icons.Default.Clear),
            contentDescription = "clear")
    }
}