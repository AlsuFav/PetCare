package ru.fav.petcare.pet.details.ui.state

import java.util.Calendar

sealed class PetDetailsEffect {
    data class ShowToast(val message: String) : PetDetailsEffect()
    data class ShowErrorDialog(val message: String) : PetDetailsEffect()
    data class ShowDatePicker(
        val minDateMillis: Long,
        val maxDateMillis: Long,
        val initialDate: Calendar
    ) : PetDetailsEffect()
    data class ShowDeletePetConfirmation(val message: String) : PetDetailsEffect()
}
