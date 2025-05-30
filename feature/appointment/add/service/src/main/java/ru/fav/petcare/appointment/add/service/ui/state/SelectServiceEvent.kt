package ru.fav.petcare.appointment.add.service.ui.state

sealed class SelectServiceEvent {
    data class GetAllServicesForPet(val id: Long) : SelectServiceEvent()
    object OnBackClicked : SelectServiceEvent()
    object OnCancelClicked : SelectServiceEvent()
    data class OnServiceClicked(val petId: Long, val serviceId: Long) : SelectServiceEvent()
}
