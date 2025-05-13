package ru.fav.petcare.safety.ui.state

sealed class UpdatePasswordState {
    object Initial : UpdatePasswordState()
    object Loading : UpdatePasswordState()
    object Success : UpdatePasswordState()

    sealed class Error : UpdatePasswordState() {
        data class FieldError(val message: String) : Error()
        data class GlobalError(val message: String) : Error()
    }
}
