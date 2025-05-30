package ru.fav.petcare.appointment.add.confirm.ui.state

sealed class ConfirmAppointmentEvent {
    object OnBackClicked : ConfirmAppointmentEvent()
    object OnCancelClicked : ConfirmAppointmentEvent()
    data class GetConfirmAppointmentData(
        val petId: Long,
        val serviceId: Long,
        val timeSlotId: Long
    ) : ConfirmAppointmentEvent()
    data class OnConfirmClicked(
        val petId: Long,
        val serviceId: Long,
        val timeSlotId: Long
    ) : ConfirmAppointmentEvent()
}
