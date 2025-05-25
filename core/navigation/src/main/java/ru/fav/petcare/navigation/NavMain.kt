package ru.fav.petcare.navigation

interface NavMain {

    fun initNavMain(parent: Nav)

    fun goBack(): Boolean

    fun goToSplashPage()

    fun goToAuthorizationPage()

    fun goToRegistrationPage()

    fun goToHomePage()

    fun goToProfilePage()

    fun goToSafetyPage()

    fun goToAllPetsPage()

    fun goToAddPetPage()

    fun goToPetDetailsPage(id: Long)
}
