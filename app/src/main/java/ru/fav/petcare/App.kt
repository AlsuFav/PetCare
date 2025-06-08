package ru.fav.petcare

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp
import ru.fav.petcare.app.BuildConfig
import ru.fav.petcare.notification.NotificationHelper
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(BuildConfig.MAPKIT_API_KEY)
        notificationHelper.createChannelsIfNeeded()
    }
}