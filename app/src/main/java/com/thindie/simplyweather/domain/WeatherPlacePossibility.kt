package com.thindie.simplyweather.domain

import androidx.compose.runtime.Stable

@Stable
data class WeatherPlacePossibility(
    val displayName: String,
    val latitude: String,
    val longitude: String,
    val name: String
)