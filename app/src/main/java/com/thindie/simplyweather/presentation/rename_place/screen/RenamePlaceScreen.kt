package com.thindie.simplyweather.presentation.rename_place.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.thindie.simplyweather.di.DependenciesProvider
import com.thindie.simplyweather.presentation.SimpleSnackBar
import com.thindie.simplyweather.presentation.TransitionState
import com.thindie.simplyweather.presentation.add_place.screen.AppOutlinedTextField
import com.thindie.simplyweather.presentation.rename_place.event.RenamePlaceScreenEvent
import com.thindie.simplyweather.presentation.rename_place.state.RenamePlaceScreenState
import com.thindie.simplyweather.presentation.rename_place.viewmodel.RenamePlaceViewModel
import com.thindie.simplyweather.routing.AppRouter
import com.thindie.simplyweather.routing.OnBackPressedHandler
import com.thindie.simplyweather.shimmerEffect

fun NavGraphBuilder.renamePlaceScreen() {
    composable(
        route = AppRouter.RouteEvent.RenamePlace::class.java.name.plus("/{coordinates}"),
        arguments = listOf(navArgument("coordinates") { type = NavType.StringType })
    ) {
        val arguments = it.arguments?.getString("coordinates")
        val args = arguments?.removePrefix("{coordinates-")
        val latitude = args?.substringBefore("_")
        val longitude = args?.substringAfter("_")?.removeSuffix("}")
        val dependenciesProvider =
            (LocalContext.current as DependenciesProvider.DependenciesHolder).getDependenciesProvider()
        val viewModel: RenamePlaceViewModel = viewModel(
            factory = dependenciesProvider.getRenameScreenViewModelFactory(
                latitude.orEmpty(), longitude.orEmpty()
            )
        )
        Screen(viewModel)
    }
}

@Composable
private fun Screen(viewModel: RenamePlaceViewModel) {
    OnBackPressedHandler(onBack = { viewModel.onEvent(RenamePlaceScreenEvent.OnBack) })
    val state by viewModel.state.collectAsStateWithLifecycle(
        initialValue = RenamePlaceScreenState(), minActiveState = Lifecycle.State.RESUMED
    )
    LaunchedEffect(key1 = Unit) {
        viewModel.onEvent(RenamePlaceScreenEvent.OnRequestRenameTitle)
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (state.isTitleSuccessfulRenamed) {
            SimpleSnackBar(
                modifier = Modifier
                    .zIndex(2f)
                    .align(Alignment.TopCenter),
                isSuccess = true,
                message = "место успешно переименовано",
                onDismiss = {
                    viewModel.onEvent(RenamePlaceScreenEvent.DismissSnack)
                }
            )
        }
        when (state.renameTransitionState) {
            TransitionState.Content -> {
                Text(
                    modifier = Modifier
                        .padding(all = 24.dp)
                        .align(Alignment.TopStart),
                    text = "Переименовать",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                )
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 24.dp)
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f)
                ) {

                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 24.dp),
                            text = state.title,
                            fontWeight = FontWeight.Black,
                            style = MaterialTheme.typography.headlineMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        AppOutlinedTextField(
                            transitionState = state.renameTransitionState,
                            value = state.title,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            onValueChange = {
                                viewModel.onEvent(
                                    RenamePlaceScreenEvent.RenameTitle(
                                        it
                                    )
                                )
                            },
                            onClickTrailingIcon = { viewModel.onEvent(RenamePlaceScreenEvent.ClearInputField) },
                            isError = state.title.isBlank(),
                            placeHolder = {
                                Text(
                                    text = "название..",
                                    fontStyle = FontStyle.Italic,
                                    fontWeight = FontWeight.Light,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            onError = {
                                Text(
                                    fontWeight = FontWeight.Light,
                                    fontFamily = FontFamily.Monospace,
                                    style = MaterialTheme.typography.labelSmall,
                                    text = "поле не может быть пустым"
                                )
                            }
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ClickableText(text = AnnotatedString("Вернуться"),
                        modifier = Modifier,

                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Light,
                        ),
                        onClick = {
                            viewModel.onEvent(
                                RenamePlaceScreenEvent.OnBack
                            )
                        }
                    )
                    Spacer(modifier = Modifier.width(24.dp))
                    ClickableText(text = AnnotatedString("Принять"),
                        modifier = Modifier,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 18.sp,
                            color = if (state.title.isNotBlank()) {
                                LocalTextStyle.current.color
                            } else {
                                LocalTextStyle.current.color.copy(
                                    0.3f
                                )
                            }
                        ),
                        onClick = {
                            if (state.title.isNotBlank()) {
                                viewModel.onEvent(
                                    RenamePlaceScreenEvent.ApplyRenameTitle
                                )
                            }
                        }
                    )
                }

            }

            TransitionState.Error -> {}
            TransitionState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth(0.7f)
                            .height(36.dp)
                            .shimmerEffect(MaterialTheme.shapes.medium)
                    )
                }
            }

            TransitionState.None -> {}
        }
    }
}