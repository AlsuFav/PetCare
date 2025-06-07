package ru.fav.petcare.service.ui.state

sealed class ServicesEffect {
    data class ShowErrorDialog(val message: String) : ServicesEffect()
}
