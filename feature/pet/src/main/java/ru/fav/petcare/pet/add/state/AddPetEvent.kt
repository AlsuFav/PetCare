package ru.fav.petcare.pet.add.state

import java.util.Calendar

sealed class AddPetEvent {
    object OnDateClicked : AddPetEvent()
    object OnCancelClicked : AddPetEvent()
    data class OnAddPetClicked(
        val name: String,
        val species: String,
        val breed: String?,
        val birthDate: String) : AddPetEvent()
    data class OnDateSelected(val calendar: Calendar) : AddPetEvent()
    object LoadBreeds : AddPetEvent()
}
