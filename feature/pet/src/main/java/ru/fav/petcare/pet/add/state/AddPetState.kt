package ru.fav.petcare.pet.add.state


sealed class AddPetState {
    object Initial : AddPetState()
    object Loading : AddPetState()
    object Success : AddPetState()

    sealed class Error : AddPetState() {
        data class FieldError(val message: String) : Error()
        object GlobalError : Error()
    }
}
