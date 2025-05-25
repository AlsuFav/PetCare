package ru.fav.petcare.pet.details.state

import java.util.Calendar

sealed class PetDetailsEvent {
    data class GetPetData(val id: Long) : PetDetailsEvent()
    data class OnSaveClicked(
        val id: Long,
        val name: String,
        val birthDate: String,
    ) : PetDetailsEvent()
    object OnBackClicked : PetDetailsEvent()
    object OnDateClicked : PetDetailsEvent()
    object OnDeletePetClicked : PetDetailsEvent()
    data class OnDateSelected(val calendar: Calendar) : PetDetailsEvent()
    data class OnConfirmDeletePetClicked(val id: Long) : PetDetailsEvent()
}
