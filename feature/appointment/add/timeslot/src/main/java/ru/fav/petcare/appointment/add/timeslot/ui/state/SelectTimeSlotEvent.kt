package ru.fav.petcare.appointment.add.timeslot.ui.state

sealed class SelectTimeSlotEvent {
    object GetAllTimeSlots : SelectTimeSlotEvent()
    object OnBackClicked : SelectTimeSlotEvent()
    object OnCancelClicked : SelectTimeSlotEvent()
    data class OnTimeSlotClicked(
        val petId: Long,
        val serviceId: Long,
        val timeSlotId: Long
    ) : SelectTimeSlotEvent()
}
