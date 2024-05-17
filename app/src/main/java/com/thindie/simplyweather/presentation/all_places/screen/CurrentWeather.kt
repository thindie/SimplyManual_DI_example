package com.thindie.simplyweather.presentation.all_places.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.thindie.simplyweather.domain.CurrentWeather
import java.time.format.DateTimeFormatter

@Composable
fun CurrentWeather(
    modifier: Modifier = Modifier,
    currentWeather: CurrentWeather,
    onClick: () -> Unit,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp),
        shadowElevation = 8.dp,
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .fillMaxHeight(0.9f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.align(Alignment.Start),
                text = currentWeather.apparentTemperature.toString(),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Black
            )
            Text(
                text = DateTimeFormatter.ISO_DATE_TIME.parse(currentWeather.time).toString(),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Thin,
            )
        }
    }
}