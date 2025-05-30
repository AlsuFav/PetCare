package ru.fav.petcare.appointment.add.service.ui.state

sealed class SelectServiceEffect {
    data class ShowErrorDialog(val message: String) : SelectServiceEffect()
}
