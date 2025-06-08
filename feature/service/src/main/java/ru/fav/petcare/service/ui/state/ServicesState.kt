package ru.fav.petcare.service.ui.state

import ru.fav.petcare.domain.model.ServiceModel

sealed class ServicesState {
    object Loading : ServicesState()
    data class Success(val services: List<ServiceModel>) : ServicesState()

    sealed class Error : ServicesState() {
        data class FieldError(val message: String) : Error()
        object GlobalError : Error()
    }
}
