package ru.fav.petcare.pet.add.ui.state

import java.util.Calendar

sealed class AddPetEffect {
    data class ShowDatePicker(
        val minDateMillis: Long,
        val maxDateMillis: Long,
        val initialDate: Calendar
        ) : AddPetEffect()
    data class ShowToast(val message: String) : AddPetEffect()
    data class ShowErrorDialog(val message: String) : AddPetEffect()
}
