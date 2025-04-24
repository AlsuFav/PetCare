package ru.fav.petcare.presentation.screens.registration

sealed class RegistrationState {
    object Initial : RegistrationState()
    object Loading : RegistrationState()
    object Success : RegistrationState()

    sealed class Error : RegistrationState() {
        data class FieldError(val message: String) : Error()
        data class GlobalError(val message: String) : Error()
    }
}
