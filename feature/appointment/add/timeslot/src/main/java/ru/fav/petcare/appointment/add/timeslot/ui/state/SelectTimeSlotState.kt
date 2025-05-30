package ru.fav.petcare.appointment.add.timeslot.ui.state

import ru.fav.petcare.domain.model.TimeSlotModel

sealed class SelectTimeSlotState {
    object Loading : SelectTimeSlotState()
    data class Success(val timeslots: List<Pair<String, List<TimeSlotModel>>>) : SelectTimeSlotState()

    sealed class Error : SelectTimeSlotState() {
        data class FieldError(val message: String) : Error()
        object GlobalError : Error()
    }
}
