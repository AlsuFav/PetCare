package ru.fav.petcare.appointment.details.ui.state

import java.util.Calendar

sealed class AppointmentDetailsEffect {
    data class ShowToast(val message: String) : AppointmentDetailsEffect()
    data class ShowErrorDialog(val message: String) : AppointmentDetailsEffect()
    data class ShowCancelAppointmentConfirmation(val message: String) : AppointmentDetailsEffect()
}
