package ru.fav.petcare.appointment.details.ui.state

import java.util.Calendar

sealed class AppointmentDetailsEvent {
    data class GetAppointmentData(val id: Long) : AppointmentDetailsEvent()
    object OnBackClicked : AppointmentDetailsEvent()
    object OnCancelAppointmentClicked : AppointmentDetailsEvent()
    data class OnConfirmCancelAppointmentClicked(val id: Long) : AppointmentDetailsEvent()
}
