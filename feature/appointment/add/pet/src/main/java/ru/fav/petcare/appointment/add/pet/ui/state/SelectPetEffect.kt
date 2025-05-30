package ru.fav.petcare.appointment.add.pet.ui.state

sealed class SelectPetEffect {
    data class ShowErrorDialog(val message: String) : SelectPetEffect()
}
