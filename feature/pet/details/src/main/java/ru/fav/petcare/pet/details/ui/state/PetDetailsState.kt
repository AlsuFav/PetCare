package ru.fav.petcare.pet.details.ui.state

import ru.fav.petcare.domain.model.PetModel


sealed class PetDetailsState {
    object Loading : PetDetailsState()
    data class Success(val pet: PetModel) : PetDetailsState()
    object Error : PetDetailsState()
}
