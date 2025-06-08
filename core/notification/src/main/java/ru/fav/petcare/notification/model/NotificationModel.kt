package ru.fav.petcare.notification.model

import ru.fav.petcare.notification.util.NotificationType

data class NotificationModel(
    val id: Int,
    val title: String,
    val message: String,
    val notificationType: NotificationType
)