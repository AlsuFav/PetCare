package ru.fav.petcare.navigation

interface NavMain {

    fun initNavMain(parent: Nav)

    fun goBack(): Boolean

    fun goToSplashPage()

    fun goToAuthorizationPage()

    fun goToRegistrationPage()

    fun goToHomePage()

    fun goToServicesPage()

    fun goToProfilePage()

    fun goToSafetyPage()

    fun goToAllPetsPage()

    fun goToAddPetPage()

    fun goToPetDetailsPage(id: Long)

    fun goToAllAppointmentsPage()

    fun goToSelectPetPage()

    fun goToSelectServicePage(petId: Long)

    fun goToSelectTimeslotPage(petId: Long, serviceId: Long)

    fun goToConfirmAppointmentPage(
        petId: Long,
        serviceId: Long,
        timeSlotId: Long
    )

    fun goToAppointmentDetailsPage(id: Long)
}
