package ru.fav.petcare.notification

import android.app.PendingIntent
import ru.fav.petcare.notification.model.NotificationModel

interface NotificationHelper {
    fun createChannelsIfNeeded()
    fun showNotification(notification: NotificationModel, pendingIntent: PendingIntent? = null)
}