package ru.fav.petcare.notification.mapper

import ru.fav.petcare.domain.provider.ResourceProvider
import ru.fav.petcare.notification.model.NotificationChannelModel
import ru.fav.petcare.notification.util.NotificationType
import javax.inject.Inject

class NotificationChannelMapper @Inject constructor(
    private val resourceProvider: ResourceProvider
) {

    fun map(
        nameRes: Int,
        importance: Int,
        type: NotificationType
    ): NotificationChannelModel {
        return NotificationChannelModel(
            id = type.name.lowercase(),
            name = resourceProvider.getString(nameRes),
            importance = importance
        )
    }
}