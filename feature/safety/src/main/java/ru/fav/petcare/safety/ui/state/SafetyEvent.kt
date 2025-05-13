package ru.fav.petcare.safety.ui.state

sealed class SafetyEvent {
    object OnBackClicked : SafetyEvent()
    data class OnChangePasswordClicked(
        val currentPassword: String,
        val newPassword: String,
        val confirmNewPassword: String
    ) : SafetyEvent()
    object OnDeleteAccountClicked : SafetyEvent()
    object OnConfirmDeleteAccountClicked : SafetyEvent()
}
