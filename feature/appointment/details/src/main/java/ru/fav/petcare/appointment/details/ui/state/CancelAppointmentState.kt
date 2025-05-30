package ru.fav.petcare.appointment.details.ui.state

sealed class CancelAppointmentState {
    object Initial : CancelAppointmentState()
    object Loading : CancelAppointmentState()
    object Success : CancelAppointmentState()
    object Error : CancelAppointmentState()
}
