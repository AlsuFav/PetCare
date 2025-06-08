package ru.fav.petcare.notification

import android.app.NotificationManager
import ru.fav.petcare.notification.mapper.NotificationChannelMapper
import ru.fav.petcare.notification.model.NotificationChannelModel
import javax.inject.Inject
import ru.fav.petcare.app.R
import ru.fav.petcare.notification.util.NotificationType

class NotificationChannelsProviderImpl @Inject constructor(
    private val mapper: NotificationChannelMapper,
) : NotificationChannelsProvider {

    override fun provideChannels(): List<NotificationChannelModel> = listOf(
        mapper.map(
            nameRes = R.string.default_importance,
            importance = NotificationManager.IMPORTANCE_DEFAULT,
            type = NotificationType.DEFAULT
        ),
        mapper.map(
            nameRes = R.string.high_importance,
            importance = NotificationManager.IMPORTANCE_HIGH,
            type = NotificationType.HIGH
        )
    )

    override fun getChannelForType(type: NotificationType): NotificationChannelModel {
        return provideChannels().first { it.id == type.name.lowercase() }
    }
}