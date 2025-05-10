package ru.fav.petcare.profile.ui.state

sealed class ClearJwtState {
    object Initial : ClearJwtState()
    object Loading : ClearJwtState()
    object Success : ClearJwtState()

    data class Error(val message: String) : ClearJwtState()
}
