package ru.fav.petcare.service.ui.state

sealed class ServicesEvent {
    object GetAllServices : ServicesEvent()
    object OnBackClicked : ServicesEvent()
}
