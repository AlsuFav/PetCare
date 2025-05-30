package ru.fav.petcare.appointment.add.service.ui.state

import ru.fav.petcare.domain.model.ServiceModel

sealed class SelectServiceState {
    object Loading : SelectServiceState()
    data class Success(val services: List<ServiceModel>) : SelectServiceState()

    sealed class Error : SelectServiceState() {
        data class FieldError(val message: String) : Error()
        object GlobalError : Error()
    }
}
