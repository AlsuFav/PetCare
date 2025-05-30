package ru.fav.petcare.domain.repository

import ru.fav.petcare.domain.model.AppointmentModel
import ru.fav.petcare.domain.model.PetModel

interface AppointmentRepository {
    suspend fun getAllUpcomingAppointmentsData() : List<AppointmentModel>
    suspend fun getAllPassedAppointmentsData() : List<AppointmentModel>
    suspend fun getAppointmentData(id: Long) : AppointmentModel
    suspend fun cancelAppointment(id: Long)
    suspend fun createAppointment(
        petId: Long,
        serviceId: Long,
        timeSlotId: Long,
        )
    suspend fun getAppointmentConfirmationData(
        petId: Long,
        serviceId: Long,
        timeSlotId: Long,
    ) : AppointmentModel
}