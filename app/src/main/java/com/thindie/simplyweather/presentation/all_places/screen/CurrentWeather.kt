package com.thindie.simplyweather.presentation.all_places.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thindie.simplyweather.domain.CurrentWeather
import com.thindie.simplyweather.presentation.add_place.screen.HeightSpacer
import com.thindie.simplyweather.presentation.theme.SimplyWeatherTheme
import java.time.Instant

@Composable
fun CurrentWeather(
    modifier: Modifier = Modifier,
    currentWeather: CurrentWeather,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
            shadowElevation = 8.dp,
            tonalElevation = 8.dp,
            onClick = onClick,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = currentWeather.apparentTemperature.toString(),
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "Калининград",
                    modifier = Modifier.align(Alignment.CenterVertically),
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
        HeightSpacer(dp = 16.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CurrentWeatherAdditionalInformation(
                information = currentWeather.precipitation.toString(),
                painter = rememberVectorPainter(image = Icons.Rounded.Warning)
            )
            Text(
                text = currentWeather.relativeHumidity2m.toString(),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Thin
            )
            Text(
                text = currentWeather.snowfall.toString(),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Thin
            )
            Text(
                text = currentWeather.windSpeed10m.toString(),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Thin
            )
        }
    }
}

@Composable
private fun CurrentWeatherAdditionalInformation(
    modifier: Modifier = Modifier,
    information: String,
    painter: Painter,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = information,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Thin
        )
        HeightSpacer(dp = 4.dp)
        Icon(
            modifier = modifier.size(12.dp),
            painter = painter,
            contentDescription = "information painter"
        )
    }
}

@Preview
@Composable
private fun PreviewCurrentWeather() {
    SimplyWeatherTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CurrentWeather(
                currentWeather = CurrentWeather(
                    latitude = 16.17f,
                    longitude = 18.19f,
                    apparentTemperature = 20.21,
                    interval = 7444,
                    isDay = false,
                    precipitation = 22.23,
                    rain = 24.25,
                    relativeHumidity2m = 2863,
                    snowfall = 26.27,
                    time = Instant.now().toString(),
                    weatherCode = 6527,
                    windDirection10m = 9603,
                    windGusts10m = 28.29,
                    windSpeed10m = 30.31,
                ), onClick = {}
            )
        }
    }
}