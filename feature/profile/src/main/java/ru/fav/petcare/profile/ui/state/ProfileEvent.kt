package ru.fav.petcare.profile.ui.state

sealed class ProfileEvent {
    object GetClientData : ProfileEvent()
    data class OnSaveClicked(
        val firstName: String,
        val lastName: String,
        val phone: String
    ) : ProfileEvent()
    object OnSafetyClicked : ProfileEvent()
    object OnLogOutClicked : ProfileEvent()
}
