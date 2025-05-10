package ru.fav.petcare.profile.ui.state

sealed class ProfileEvent {
    object OnLogOutClicked : ProfileEvent()
}
