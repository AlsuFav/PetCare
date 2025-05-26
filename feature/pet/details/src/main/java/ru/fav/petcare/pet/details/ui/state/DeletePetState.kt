package ru.fav.petcare.pet.details.ui.state

sealed class DeletePetState {
    object Initial : DeletePetState()
    object Loading : DeletePetState()
    object Success : DeletePetState()
    object Error : DeletePetState()
}
