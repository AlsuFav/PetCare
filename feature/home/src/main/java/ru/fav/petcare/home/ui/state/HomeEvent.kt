package ru.fav.petcare.home.ui.state

sealed class HomeEvent {
    object OnServicesClicked : HomeEvent()
    object GetData : HomeEvent()
}
