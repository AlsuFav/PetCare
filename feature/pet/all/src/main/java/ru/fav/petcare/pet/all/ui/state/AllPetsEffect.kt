package ru.fav.petcare.pet.all.ui.state

sealed class AllPetsEffect {
    data class ShowErrorDialog(val message: String) : AllPetsEffect()
}
