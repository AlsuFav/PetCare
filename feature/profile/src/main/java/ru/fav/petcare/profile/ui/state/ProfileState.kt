package ru.fav.petcare.profile.ui.state

import ru.fav.petcare.domain.model.ClientModel


sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(val client: ClientModel) : ProfileState()
    data class Error(val message: String) : ProfileState()
}
