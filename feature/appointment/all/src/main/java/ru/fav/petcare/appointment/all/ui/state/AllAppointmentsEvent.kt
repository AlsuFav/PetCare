package ru.fav.petcare.appointment.all.ui.state

sealed class AllAppointmentsEvent {
    object GetAllUpcomingAppointments : AllAppointmentsEvent()
    object GetAllPassedAppointments : AllAppointmentsEvent()
    object OnAddAppointmentClicked : AllAppointmentsEvent()
    data class OnAppointmentClicked(val id: Long) : AllAppointmentsEvent()
}
