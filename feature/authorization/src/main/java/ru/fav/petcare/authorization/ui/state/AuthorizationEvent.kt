package ru.fav.petcare.authorization.ui.state

sealed class AuthorizationEvent {
    data class OnSignInClicked(val phone: String, val password: String) : AuthorizationEvent()
    object OnSignUpClicked : AuthorizationEvent()
}

