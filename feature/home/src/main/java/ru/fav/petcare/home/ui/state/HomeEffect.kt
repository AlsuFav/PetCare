package ru.fav.petcare.home.ui.state

sealed class HomeEffect {
    data class ShowErrorDialog(val message: String) : HomeEffect()
}
