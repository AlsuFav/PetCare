package ru.fav.petcare.profile.ui.state

sealed class ProfileEffect {
    data class ShowToast(val message: String) : ProfileEffect()
}
