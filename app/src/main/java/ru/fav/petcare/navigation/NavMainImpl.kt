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

    override fun goToServicesPage() {
        navigatorDelegate.navigate(action = R.id.action_global_services)
    }

    override fun goToProfilePage() {
        navigatorDelegate.navigate(action = R.id.action_global_profile)
    }

    override fun goToSafetyPage() {
        navigatorDelegate.navigate(action = R.id.action_global_safety)
    }

    override fun goToAllPetsPage() {
        navigatorDelegate.navigate(action = R.id.action_global_all_pets)
    }

    override fun goToAddPetPage() {
        navigatorDelegate.navigate(action = R.id.action_global_add_pet)
    }

    override fun goToPetDetailsPage(id: Long) {
        val args = Bundle().apply {
            putLong("id", id)
        }
        navigatorDelegate.navigate(
            action = R.id.action_global_pet_details,
            args = args
        )
    }

    override fun goToAllAppointmentsPage() {
        navigatorDelegate.navigate(action = R.id.action_global_all_appointments)
    }

    override fun goToSelectPetPage() {
        navigatorDelegate.navigate(action = R.id.action_global_select_pet)
    }

    override fun goToSelectServicePage(petId: Long) {
        val args = Bundle().apply {
            putLong("petId", petId)
        }
        navigatorDelegate.navigate(
            action = R.id.action_global_select_service,
            args = args
        )
    }

    override fun goToSelectTimeslotPage(petId: Long, serviceId: Long) {
        val args = Bundle().apply {
            putLong("petId", petId)
            putLong("serviceId", serviceId)
        }
        navigatorDelegate.navigate(
            action = R.id.action_global_select_timeslot,
            args = args
        )
    }

    override fun goToConfirmAppointmentPage(
        petId: Long,
        serviceId: Long,
        timeSlotId: Long
    ) {
        val args = Bundle().apply {
            putLong("petId", petId)
            putLong("serviceId", serviceId)
            putLong("timeSlotId", timeSlotId)
        }
        navigatorDelegate.navigate(
            action = R.id.action_global_confirm_appointment,
            args = args
        )
    }

    override fun goToAppointmentDetailsPage(id: Long) {
        val args = Bundle().apply {
            putLong("id", id)
        }
        navigatorDelegate.navigate(
            action = R.id.action_global_appointment_details,
            args = args
        )
    }
}
