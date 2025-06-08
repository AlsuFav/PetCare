package ru.fav.petcare.notification

import ru.fav.petcare.notification.model.NotificationChannelModel
import ru.fav.petcare.notification.util.NotificationType

interface NotificationChannelsProvider {
    fun provideChannels(): List<NotificationChannelModel>
    fun getChannelForType(type: NotificationType): NotificationChannelModel
}