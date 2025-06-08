package ru.fav.petcare.notification.model

data class NotificationChannelModel(
    val id: String,
    val name: String,
    val importance: Int
)