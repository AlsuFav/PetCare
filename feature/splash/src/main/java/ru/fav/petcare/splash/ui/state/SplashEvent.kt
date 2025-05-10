package ru.fav.petcare.splash.ui.state

sealed class SplashEvent {
    object CheckApiKey : SplashEvent()
}
