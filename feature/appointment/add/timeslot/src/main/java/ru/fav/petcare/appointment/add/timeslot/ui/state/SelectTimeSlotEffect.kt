package ru.fav.petcare.appointment.add.timeslot.ui.state

sealed class SelectTimeSlotEffect {
    data class ShowErrorDialog(val message: String) : SelectTimeSlotEffect()
}
