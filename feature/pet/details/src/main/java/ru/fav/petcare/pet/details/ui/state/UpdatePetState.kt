package ru.fav.petcare.pet.details.ui.state

sealed class UpdatePetState {
    object Initial : UpdatePetState()
    object Loading : UpdatePetState()
    object Success : UpdatePetState()

    sealed class Error : UpdatePetState() {
        data class FieldError(val message: String) : Error()
        object GlobalError : Error()
    }
}
