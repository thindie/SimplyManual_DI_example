package com.thindie.simplyweather.presentation.detail_place.event

sealed class DetailPlaceScreenEvent {
    data object OnBackClick: DetailPlaceScreenEvent()
}