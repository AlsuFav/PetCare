package ru.fav.petcare.appointment.all.ui.state

import ru.fav.petcare.domain.model.AppointmentModel

sealed class AllAppointmentsState {
    object Loading : AllAppointmentsState()
    data class Success(val appointments: List<AppointmentModel>) : AllAppointmentsState()

    sealed class Error : AllAppointmentsState() {
        data class FieldError(val message: String) : Error()
        object GlobalError : Error()
    }
}
