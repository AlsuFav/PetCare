package ru.fav.petcare.appointment.add.pet.ui.state

sealed class SelectPetEvent {
    object GetAllPets : SelectPetEvent()
    object OnAddPetClicked : SelectPetEvent()
    object OnCancelClicked : SelectPetEvent()
    data class OnPetClicked(val id: Long) : SelectPetEvent()
}
