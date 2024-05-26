package com.thindie.simplyweather.presentation.stored_places.event

sealed class StoredPlacesEvent {
    data object RequestStoredPlaces: StoredPlacesEvent()
    data object OnBack: StoredPlacesEvent()

}