package ru.fav.petcare.pet.all.state

sealed class AllPetsEffect {
    data class ShowErrorDialog(val message: String) : AllPetsEffect()
}
