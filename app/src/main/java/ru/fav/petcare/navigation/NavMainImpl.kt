package ru.fav.petcare.navigation

import android.os.Bundle
import ru.fav.petcare.app.R
import javax.inject.Inject

class NavMainImpl @Inject constructor(
    private val navigatorDelegate: NavigatorDelegate,
) : NavMain {

    private var parent: Nav? = null

    override fun initNavMain(parent: Nav) {
        this.parent = parent
    }

    override fun goBack(): Boolean {
        return navigatorDelegate.navigateBack()
    }

    override fun goToSplashPage() {
        navigatorDelegate.navigate(action = R.id.action_global_splash)
    }

    override fun goToAuthorizationPage() {
        navigatorDelegate.navigate(action = R.id.action_global_authorization)
    }

    override fun goToRegistrationPage() {
        navigatorDelegate.navigate(action = R.id.action_global_registration)
    }

    override fun goToHomePage() {
        navigatorDelegate.navigate(action = R.id.action_global_home)
    }

    override fun goToProfilePage() {
        navigatorDelegate.navigate(action = R.id.action_global_profile)
    }
}
