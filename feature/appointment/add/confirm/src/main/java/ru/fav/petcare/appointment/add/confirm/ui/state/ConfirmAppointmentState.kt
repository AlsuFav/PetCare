package ru.fav.petcare.appointment.add.confirm.ui.state

sealed class ConfirmAppointmentState {
    object Initial : ConfirmAppointmentState()
    object Loading : ConfirmAppointmentState()
    object Success : ConfirmAppointmentState()
    object Error : ConfirmAppointmentState()
}
