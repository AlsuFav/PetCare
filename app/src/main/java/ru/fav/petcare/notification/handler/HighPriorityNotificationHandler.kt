package ru.fav.petcare.notification.handler

import ru.fav.petcare.domain.provider.ResourceProvider
import ru.fav.petcare.notification.FcmMessageHandler
import ru.fav.petcare.notification.NotificationHelper
import ru.fav.petcare.app.R
import ru.fav.petcare.notification.model.NotificationModel
import ru.fav.petcare.notification.util.NotificationType
import ru.fav.petcare.notification.util.FcmCategories
import ru.fav.petcare.notification.util.FcmDataKeys
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HighPriorityNotificationHandler @Inject constructor(
    private val notificationHelper: NotificationHelper,
    private val resourceProvider: ResourceProvider
) : FcmMessageHandler {

    override fun canHandle(category: String): Boolean = 
        category == FcmCategories.HIGH_PRIORITY_NOTIFICATION

    override fun handle(data: Map<String, String>) {
        val title = data[FcmDataKeys.TITLE] ?: resourceProvider.getString(R.string.default_notification_title)
        val message = data[FcmDataKeys.MESSAGE] ?: resourceProvider.getString(R.string.default_notification_message)
        val notificationId = data[FcmDataKeys.ID]?.toIntOrNull() ?: System.currentTimeMillis().toInt()

        val notificationData = NotificationModel(
            id = notificationId,
            title = title,
            message = message,
            notificationType = NotificationType.HIGH
        )
        notificationHelper.showNotification(notificationData)
    }
}