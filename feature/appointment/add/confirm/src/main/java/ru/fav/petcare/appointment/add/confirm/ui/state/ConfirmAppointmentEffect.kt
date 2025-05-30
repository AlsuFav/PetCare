package ru.fav.petcare.appointment.add.confirm.ui.state

sealed class ConfirmAppointmentEffect {
    data class ShowToast(val message: String) : ConfirmAppointmentEffect()
    data class ShowErrorDialog(val message: String) : ConfirmAppointmentEffect()
}
