package ru.fav.petcare.domain.usecase.appointment

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.fav.petcare.domain.di.qualifier.IoDispatchers
import ru.fav.petcare.domain.exception.NoAppointmentsException
import ru.fav.petcare.domain.model.AppointmentModel
import ru.fav.petcare.domain.repository.AppointmentRepository
import javax.inject.Inject

class GetAllPassedAppointmentsDataUseCase @Inject constructor(
    private val appointmentRepository: AppointmentRepository,
    @IoDispatchers private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): List<AppointmentModel> {
        return withContext(dispatcher) {
            val appointments = appointmentRepository.getAllPassedAppointmentsData()

            if (appointments.isEmpty()) {
                throw NoAppointmentsException(null)
            } else {
                appointments
            }
        }
    }
}