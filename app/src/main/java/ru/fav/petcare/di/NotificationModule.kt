package ru.fav.petcare.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import ru.fav.petcare.notification.FcmMessageHandler
import ru.fav.petcare.notification.NotificationChannelsProvider
import ru.fav.petcare.notification.NotificationChannelsProviderImpl
import ru.fav.petcare.notification.NotificationHelper
import ru.fav.petcare.notification.NotificationHelperImpl
import ru.fav.petcare.notification.handler.HighPriorityNotificationHandler
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NotificationModule {

    @Binds
    @Singleton
    fun bindNotificationChannelsProviderToImpl(impl: NotificationChannelsProviderImpl): NotificationChannelsProvider

    @Binds
    @Singleton
    fun bindNotificationHelperToImpl(impl: NotificationHelperImpl): NotificationHelper

    @Binds
    @IntoSet
    abstract fun bindHighPriorityHandler(handler: HighPriorityNotificationHandler): FcmMessageHandler
}
