package ru.fav.petcare.navigation

interface NavMain {

    fun initNavMain(parent: Nav)

    fun goBack(): Boolean

    fun goToAuthorizationPage()

    fun goToRegistrationPage()

    fun goToHomePage()
}
