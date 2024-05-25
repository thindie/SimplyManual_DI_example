package com.thindie.simplyweather.presentation.rename_place.event

sealed class RenamePlaceScreenEvent {

    data object OnRequestRenameTitle: RenamePlaceScreenEvent()
    data class RenameTitle(val title: String): RenamePlaceScreenEvent()

    data object ApplyRenameTitle: RenamePlaceScreenEvent()
    data object OnBack: RenamePlaceScreenEvent()
    data object ClearInputField: RenamePlaceScreenEvent()
    data object DismissSnack: RenamePlaceScreenEvent()

}