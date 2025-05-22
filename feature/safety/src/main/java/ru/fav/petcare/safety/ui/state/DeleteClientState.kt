package ru.fav.petcare.safety.ui.state

sealed class DeleteClientState {
    object Initial : DeleteClientState()
    object Loading : DeleteClientState()
    object Success : DeleteClientState()

    data class Error(val message: String) : DeleteClientState()
}
