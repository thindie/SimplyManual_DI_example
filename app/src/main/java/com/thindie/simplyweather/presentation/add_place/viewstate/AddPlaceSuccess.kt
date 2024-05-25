package com.thindie.simplyweather.presentation.add_place.viewstate

sealed class AddPlaceSuccess {
    data object SuccessAddition: AddPlaceSuccess()
}