package com.thindie.simplyweather.presentation.add_place.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thindie.simplyweather.presentation.theme.SimplyWeatherTheme
import com.thindie.simplyweather.shimmerEffect

@Composable
fun AddPlaceShimmer() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 24.dp)
    ) {
        Spacer(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(72.dp)
                .fillMaxWidth(0.8f)
                .shimmerEffect(MaterialTheme.shapes.large)
        )
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Divider(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
        )
        HeightSpacer(dp = 42.dp)
        Spacer(
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth(0.8f)
                .padding(horizontal = 24.dp)
                .shimmerEffect(MaterialTheme.shapes.large)
        )
        HeightSpacer(dp = 24.dp)
        Spacer(
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth(0.8f)
                .padding(horizontal = 24.dp)
                .shimmerEffect(MaterialTheme.shapes.large)
        )
        HeightSpacer(dp = 24.dp)
        Spacer(
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth(0.8f)
                .padding(horizontal = 24.dp)
                .shimmerEffect(MaterialTheme.shapes.large)
        )

    }
}

@Preview
@Composable
private fun previewShimmer() {
    SimplyWeatherTheme {
        AddPlaceShimmer()
    }
}