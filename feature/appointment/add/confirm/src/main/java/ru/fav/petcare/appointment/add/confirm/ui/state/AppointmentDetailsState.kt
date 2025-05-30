package ru.fav.petcare.appointment.add.confirm.ui.state

import ru.fav.petcare.domain.model.AppointmentModel

sealed class AppointmentDetailsState {
    object Loading : AppointmentDetailsState()
    data class Success(val appointment: AppointmentModel) : AppointmentDetailsState()
    object Error : AppointmentDetailsState()
}
