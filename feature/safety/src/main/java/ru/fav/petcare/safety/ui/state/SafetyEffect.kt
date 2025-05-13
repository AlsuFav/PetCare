package ru.fav.petcare.safety.ui.state

sealed class SafetyEffect {
    data class ShowToast(val message: String) : SafetyEffect()
    data class ShowDeleteAccountConfirmation(val message: String) : SafetyEffect()
}
