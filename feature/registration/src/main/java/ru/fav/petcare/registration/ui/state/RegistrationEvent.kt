package ru.fav.petcare.registration.ui.state

sealed class RegistrationEvent {
    data class OnSignUpClicked(
        val firstName: String,
        val lastName: String,
        val phone: String,
        val password: String,
        val confirmPassword: String
    ) : RegistrationEvent()
    object OnSignInClicked : RegistrationEvent()
}