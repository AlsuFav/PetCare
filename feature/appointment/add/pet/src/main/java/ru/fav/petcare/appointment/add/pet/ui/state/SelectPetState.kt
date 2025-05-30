package ru.fav.petcare.appointment.add.pet.ui.state

import ru.fav.petcare.domain.model.PetModel

sealed class SelectPetState {
    object Loading : SelectPetState()
    data class Success(val pets: List<PetModel>) : SelectPetState()

    sealed class Error : SelectPetState() {
        data class FieldError(val message: String) : Error()
        object GlobalError : Error()
    }
}
