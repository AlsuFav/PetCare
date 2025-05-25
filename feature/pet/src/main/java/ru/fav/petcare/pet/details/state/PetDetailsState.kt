package ru.fav.petcare.pet.details.state

import ru.fav.petcare.domain.model.PetModel


sealed class PetDetailsState {
    object Loading : PetDetailsState()
    data class Success(val pet: PetModel) : PetDetailsState()
    object Error : PetDetailsState()
}
