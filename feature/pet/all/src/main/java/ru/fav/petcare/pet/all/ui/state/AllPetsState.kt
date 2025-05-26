package ru.fav.petcare.pet.all.ui.state

import ru.fav.petcare.domain.model.PetModel

sealed class AllPetsState {
    object Loading : AllPetsState()
    data class Success(val pets: List<PetModel>) : AllPetsState()

    sealed class Error : AllPetsState() {
        data class FieldError(val message: String) : Error()
        object GlobalError : Error()
    }
}
