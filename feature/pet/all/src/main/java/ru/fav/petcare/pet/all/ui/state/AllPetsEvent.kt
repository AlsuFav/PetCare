package ru.fav.petcare.pet.all.ui.state

sealed class AllPetsEvent {
    object GetAllPets : AllPetsEvent()
    object OnAddPetClicked : AllPetsEvent()
    data class OnPetClicked(val id: Long) : AllPetsEvent()
}
