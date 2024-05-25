package com.thindie.simplyweather.presentation.detail_place.event

import com.thindie.simplyweather.domain.DailyForecast

sealed class DetailPlaceScreenEvent {
    data object OnBackClick : DetailPlaceScreenEvent()
    data class ChangePlaceTitle(val title: String) :
        DetailPlaceScreenEvent()

    data object DeletePlace : DetailPlaceScreenEvent()
    data class RequestHourlyForecast(val dailyForecast: DailyForecast) : DetailPlaceScreenEvent()
    data object RequestDetailForecast : DetailPlaceScreenEvent()
    data object TriggerDropDownMenu : DetailPlaceScreenEvent()
    data object TriggerChangeTitle : DetailPlaceScreenEvent()
    data object DismissHourlyForecast: DetailPlaceScreenEvent()

}
