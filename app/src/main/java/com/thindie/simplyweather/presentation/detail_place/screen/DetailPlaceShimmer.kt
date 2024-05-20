package com.thindie.simplyweather.presentation.detail_place.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thindie.simplyweather.presentation.add_place.screen.HeightSpacer
import com.thindie.simplyweather.presentation.theme.SimplyWeatherTheme
import com.thindie.simplyweather.shimmerEffect

@Composable
fun DetailPlaceShimmer() {
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                        .shimmerEffect(MaterialTheme.shapes.small)
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(40.dp)
                        .shimmerEffect(MaterialTheme.shapes.small)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp),
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(
                    modifier = Modifier
                        .width(80.dp)
                        .height(20.dp)
                        .shimmerEffect(MaterialTheme.shapes.small)
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(20.dp)
                        .shimmerEffect(MaterialTheme.shapes.small)
                )
            }
            Divider()
            HeightSpacer(dp = 36.dp)
            repeat(3) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(
                        modifier = Modifier
                            .width(40.dp)
                            .height(30.dp)
                            .shimmerEffect(MaterialTheme.shapes.small)
                    )
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(30.dp)
                            .shimmerEffect(MaterialTheme.shapes.small)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewDetailScreen() {
    SimplyWeatherTheme {
        DetailPlaceShimmer()
    }
}