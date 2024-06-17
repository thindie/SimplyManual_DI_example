package com.thindie.simplyweather.presentation.all_places.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.thindie.simplyweather.domain.CurrentWeather
import com.thindie.simplyweather.presentation.weatherNotation
import okhttp3.internal.format
@Suppress("LongParameterList")
@Composable
fun CurrentWeather(
    modifier: Modifier = Modifier,
    currentWeather: CurrentWeather,
    currentWeatherTitle: String,
    expectedPrecipitationTime: String,
    onClick: () -> Unit,
    onExpect: () -> Unit,
) {
    LaunchedEffect(key1 = currentWeather.precipitation > 0) {
        onExpect.invoke()
    }
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = currentWeather.apparentTemperature.toString(),
                modifier = Modifier,
                fontWeight = FontWeight.Black,
                style = MaterialTheme.typography.headlineLarge,
                color = if (currentWeather.apparentTemperature > 0) Color.Red else
                    Color.Black.copy(0.7f)
            )
            Spacer(modifier = Modifier.width(12.dp))
            TextButton(
                onClick = onClick,
                contentPadding = PaddingValues(4.dp),
            ) {
                Text(
                    text = currentWeatherTitle,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        }
        Text(
            modifier = Modifier.padding(horizontal = 24.dp),
            text = stringResource(id = weatherNotation(currentWeather.weatherCode)),
            style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = format("ветер: %s м/с", currentWeather.windSpeed10m.toString()),
                modifier = Modifier,
                fontWeight = FontWeight.Light,
                color = LocalTextStyle.current.color.copy(0.5f),
                style = MaterialTheme.typography.labelLarge,
            )
            Text(text = expectedPrecipitationTime)
            Text(
                text = format("осадки %s мм", currentWeather.precipitation.toString()),
                modifier = Modifier,
                fontWeight = FontWeight.Light,
                color = LocalTextStyle.current.color.copy(0.5f),
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

