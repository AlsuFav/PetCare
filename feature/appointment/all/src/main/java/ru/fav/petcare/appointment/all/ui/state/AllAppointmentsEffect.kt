package ru.fav.petcare.appointment.all.ui.state

sealed class AllAppointmentsEffect {
    data class ShowErrorDialog(val message: String) : AllAppointmentsEffect()
}
